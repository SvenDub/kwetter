package nl.svendubbeld.fontys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UncaughtExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        logger.error("An uncaught exception has been thrown.", exception);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exception)
                .build();
    }
}
