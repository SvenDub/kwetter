package nl.svendubbeld.fontys.auth;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @see <a href="https://stackoverflow.com/a/45814178">https://stackoverflow.com/a/45814178</a>
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        Class<?> clazz = resourceInfo.getResourceClass();

        if (method.isAnnotationPresent(DenyAll.class)) {
            refuseRequest(requestContext);
            return;
        }

        RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
            return;
        }

        if (method.isAnnotationPresent(PermitAll.class)) {
            return;
        }

        rolesAllowed = clazz.getAnnotation(RolesAllowed.class);
        if (rolesAllowed != null) {
            performAuthorization(rolesAllowed.value(), requestContext);
            return;
        }

        if (clazz.isAnnotationPresent(PermitAll.class)) {
            return;
        }
    }

    private void performAuthorization(String[] rolesAllowed, ContainerRequestContext requestContext) {
        if (rolesAllowed.length > 0 && !isAuthenticated(requestContext)) {
            refuseRequest(requestContext);
        }

        for (final String role : rolesAllowed) {
            if (requestContext.getSecurityContext().isUserInRole(role)) {
                return;
            }
        }

        refuseRequest(requestContext);
    }

    private boolean isAuthenticated(ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }

    private void refuseRequest(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
