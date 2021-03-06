package nl.svendubbeld.fontys.auth;

import io.jsonwebtoken.*;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @see <a href="https://stackoverflow.com/a/26778123">https://stackoverflow.com/a/26778123</a>
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    private UserService userService;

    @Inject
    private KeyGenerator keyGenerator;

    @Override
    @Transactional
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo().getQueryParameters();
        String authorizationQueryParam = queryParameters.getFirst("authToken");

        if (authorizationHeader == null && authorizationQueryParam == null) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        String token;
        if (authorizationHeader != null) {
            token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        } else {
            token = authorizationQueryParam;
        }

        User user = null;
        String username = null;

        try {
            Key key = keyGenerator.generateKey();
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            if ("access_token".equals(claimsJws.getHeader().get("use"))) {
                username = claimsJws.getBody().getSubject();
                user = userService.findByUsername(username);
            }
        } catch (SignatureException e) {
            logger.warn("Invalid signature detected!", e);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token expired", e);
        }

        if (user == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        Set<Permission> permissions = user.getSecurityGroups().stream().map(SecurityGroup::getPermissions).flatMap(Collection::stream).collect(Collectors.toSet());
        String finalUsername = username;

        SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> finalUsername;
            }

            @Override
            public boolean isUserInRole(String role) {
                return permissions.contains(Permission.of(role));
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }
}
