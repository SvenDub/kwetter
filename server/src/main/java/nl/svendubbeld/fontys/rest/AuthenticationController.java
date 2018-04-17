package nl.svendubbeld.fontys.rest;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64Codec;
import nl.svendubbeld.fontys.auth.AuthenticationFilter;
import nl.svendubbeld.fontys.auth.KeyGenerator;
import nl.svendubbeld.fontys.auth.TokenResponse;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Token;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.UUID;

@Path("/auth")
@SentryLogged
public class AuthenticationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Inject
    private KeyGenerator keyGenerator;

    @Inject
    private UserService userService;

    @POST
    @Path("/login")
    @Transactional
    public Response login(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        String[] decodedHeader = Base64Codec.BASE64.decodeToString(authHeader.substring("Basic ".length())).split(":");
        String username = decodedHeader[0];
        String password = decodedHeader[1];

        User user = userService.findByUsername(username);

        if (user == null) {
            return unauthorized();
        }

        boolean authenticated;
        try {
            authenticated = UserService.encodeSHA256(password).equals(user.getPassword());
        } catch (NoSuchAlgorithmException e) {
            logger.warn("Can't encode password.", e);
            return unauthorized();
        }

        if (!authenticated) {
            return unauthorized();
        }

        String uuid = UUID.randomUUID().toString();
        String accessToken = issueAccessToken(username, Duration.ofHours(1));
        String refreshToken = issueRefreshToken(username, uuid, Duration.ofDays(30));

        user.addToken(new Token(uuid, user, false));
        userService.edit(user);

        return ok(new TokenResponse(accessToken, refreshToken));
    }

    @POST
    @Path("/refresh")
    @Transactional
    public Response refresh(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(AuthenticationFilter.AUTHENTICATION_SCHEME.length()).trim();

        try {
            Key key = keyGenerator.generateKey();
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(refreshToken);

            if (!"refresh_token".equals(claimsJws.getHeader().get("use"))) {
                return unauthorized();
            }

            String username = claimsJws.getBody().getSubject();
            String uuid = claimsJws.getBody().getId();
            User user = userService.findByUsername(username);

            if (user == null) {
                return unauthorized();
            }

            if (user.getTokens().stream().filter(Token::isValid).noneMatch(token -> token.getUuid().equals(uuid))) {
                return unauthorized();
            }

            String accessToken = issueAccessToken(username, Duration.ofHours(1));

            return ok(new TokenResponse(accessToken, refreshToken));
        } catch (SignatureException e) {
            logger.warn("Invalid signature detected!", e);
            return unauthorized();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token expired", e);
            return unauthorized();
        }
    }

    private String issueAccessToken(String username, TemporalAmount lifetime) {
        Key key = keyGenerator.generateKey();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("use", "access_token")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(OffsetDateTime.now().plus(lifetime).toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private String issueRefreshToken(String username, String id, TemporalAmount lifetime) {
        Key key = keyGenerator.generateKey();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("use", "refresh_token")
                .setSubject(username)
                .setId(id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(OffsetDateTime.now().plus(lifetime).toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}
