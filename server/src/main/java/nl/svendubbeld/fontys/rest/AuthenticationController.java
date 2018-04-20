package nl.svendubbeld.fontys.rest;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64Codec;
import nl.svendubbeld.fontys.auth.AuthenticationFilter;
import nl.svendubbeld.fontys.auth.SignUpRequest;
import nl.svendubbeld.fontys.auth.TokenResponse;
import nl.svendubbeld.fontys.logging.SentryLogged;
import nl.svendubbeld.fontys.model.Profile;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Token;
import nl.svendubbeld.fontys.service.ProfileService;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Path("/auth")
@SentryLogged
public class AuthenticationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String ERROR_MESSAGE_KEY = "message";

    @Inject
    private UserService userService;

    @Inject
    private ProfileService profileService;

    @Inject
    private SecurityService securityService;

    @Inject
    private Validator validator;

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

        if (!authenticated) {
            return unauthorized();
        }

        return ok(securityService.login(username));
    }

    @POST
    @Path("/refresh")
    public Response refresh(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(AuthenticationFilter.AUTHENTICATION_SCHEME.length()).trim();

        try {
            Key key = securityService.generateKey();
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

            Optional<Token> tokenOptional = user.getTokens()
                    .stream()
                    .filter(Token::isValid)
                    .filter(token -> token.getUuid().equals(uuid))
                    .findFirst();
            if (!tokenOptional.isPresent()) {
                return unauthorized();
            }

            Token token = tokenOptional.get();
            token.setLastUsed(OffsetDateTime.now());
            userService.edit(user);

            String accessToken = securityService.refresh(username);

            return ok(new TokenResponse(accessToken, refreshToken));
        } catch (SignatureException e) {
            logger.warn("Invalid signature detected!", e);
            return unauthorized();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token expired", e);
            return unauthorized();
        }
    }

    @POST
    @Path("/signUp")
    public Response signUp(SignUpRequest request) {
        if (userService.exists(request.getUsername())) {
            JsonObject response = Json.createObjectBuilder()
                    .add(ERROR_MESSAGE_KEY, "The given username already exists.")
                    .build();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }

        if (userService.emailExists(request.getEmail())) {
            JsonObject response = Json.createObjectBuilder()
                    .add(ERROR_MESSAGE_KEY, "The given email already exists.")
                    .build();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        // Fun fact: If you use Collections.singleton() here to create a set, it will break JPA when you merge the entity
        user.setSecurityGroups(new HashSet<>(Collections.singletonList(securityService.findGroupByName("Default"))));

        Profile profile = user.createProfile(request.getUsername(), request.getName(), null, null, null);

        try {
            Set<ConstraintViolation<User>> userViolations = validator.validate(user);

            if (!userViolations.isEmpty()) {
                String message = userViolations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(","));
                JsonObject response = Json.createObjectBuilder()
                        .add(ERROR_MESSAGE_KEY, message)
                        .build();
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(response)
                        .build();
            }

            Set<ConstraintViolation<Profile>> profileViolations = validator.validate(profile);

            if (!profileViolations.isEmpty()) {
                String message = profileViolations.stream()
                        .map(violation -> violation.getPropertyPath().toString() + " " + violation.getMessage())
                        .collect(Collectors.joining(","));
                JsonObject response = Json.createObjectBuilder()
                        .add(ERROR_MESSAGE_KEY, message)
                        .build();
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(response)
                        .build();
            }

            userService.addUser(user);

            return ok(securityService.login(request.getUsername()));
        } catch (Exception e) {
            JsonObject response = Json.createObjectBuilder()
                    .add(ERROR_MESSAGE_KEY, "Unknown error.")
                    .build();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }
}
