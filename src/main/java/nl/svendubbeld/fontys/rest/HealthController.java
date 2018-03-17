package nl.svendubbeld.fontys.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health")
public class HealthController {

    @GET
    public Response getHealth() {
        return Response.ok().build();
    }
}
