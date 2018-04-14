package nl.svendubbeld.fontys.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import nl.svendubbeld.fontys.auth.AuthenticationFilter;
import nl.svendubbeld.fontys.auth.KeyGenerator;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Date;

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

        if (authenticated) {
            String token = issueToken(username);

            return Response.ok()
                    .header(HttpHeaders.AUTHORIZATION, AuthenticationFilter.AUTHENTICATION_SCHEME + " " + token)
                    .build();
        } else {
            return unauthorized();
        }
    }

    private String issueToken(String username) {
        Key key = keyGenerator.generateKey();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(OffsetDateTime.now().plusHours(1).toInstant().toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }
}
