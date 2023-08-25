package ansk.development.configuration.system_config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * General configuration for Object mapper.
 *
 * @author Anton Skripin
 */
public class ObjectMapperConfiguration {

    private static ObjectMapper objectMapper;

    private ObjectMapperConfiguration() {
    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }
        return objectMapper;
    }

    public static ObjectMapper getObjectMapper(Module... modules) {
        ObjectMapper objectMapper = getObjectMapper().copy();
        objectMapper.registerModules(modules);
        return objectMapper;
    }


}
