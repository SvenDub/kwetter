package nl.svendubbeld.fontys;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class JAXRsConfiguration extends ResourceConfig {

    public JAXRsConfiguration() {
        register(JacksonFeature.class);

        packages("nl.svendubbeld.fontys");
    }
}
