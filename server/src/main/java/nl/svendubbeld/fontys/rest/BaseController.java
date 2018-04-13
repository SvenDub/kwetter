package nl.svendubbeld.fontys.rest;

import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.service.UserService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.net.URI;

public abstract class BaseController {

    @Inject
    private UserService userService;

    @Inject
    private HttpServletRequest request;

    protected User getUser() {
        return userService.findByUsername(request.getHeader(Headers.API_KEY));
    }

    protected Response ok() {
        return Response.ok().build();
    }

    protected Response ok(Object entity) {
        return Response.ok(entity).build();
    }

    protected Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    protected Response created(Object entity, URI location) {
        return Response.created(location).entity(entity).build();
    }

    protected Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
