package nl.svendubbeld.fontys.rest;

import javax.ws.rs.core.Response;
import java.net.URI;

public abstract class BaseController {

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
}
