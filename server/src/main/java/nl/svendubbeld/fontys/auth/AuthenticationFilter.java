package nl.svendubbeld.fontys.auth;

import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.rest.Headers;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
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

    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Inject
    private UserService userService;

    @Override
    @Transactional
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String username = requestContext.getHeaderString(Headers.API_KEY);
        User user = userService.findByUsername(username);

        if (user == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        Set<Permission> permissions = user.getSecurityGroups().stream().map(SecurityGroup::getPermissions).flatMap(Collection::stream).collect(Collectors.toSet());

        SecurityContext currenctSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> username;
            }

            @Override
            public boolean isUserInRole(String role) {
                return permissions.contains(Permission.of(role));
            }

            @Override
            public boolean isSecure() {
                return currenctSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }
}
