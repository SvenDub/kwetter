package nl.svendubbeld.fontys;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        mapper.findAndRegisterModules();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
