package com.hicounselor.matching.core.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hicounselor.matching.core.api.Job;
import com.hicounselor.matching.core.exception.SerializationException;
import com.hicounselor.matching.core.serde.DeserializationException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new SimpleModule());
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.setVisibility(OBJECT_MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(ANY)
                .withGetterVisibility(NONE)
                .withSetterVisibility(NONE)
                .withCreatorVisibility(ANY));
        OBJECT_MAPPER.disable(FAIL_ON_EMPTY_BEANS);
        OBJECT_MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.disable(WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static byte[] writeValueAsBytes(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        }
        catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    public static String writeValueAsString(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    public static <T> T readValue(final byte[] data, final Class<T> jobClass) {
        try {
            return OBJECT_MAPPER.readValue(data, jobClass);
        }
        catch (IOException e) {
            throw new DeserializationException(e);
        }
    }
}
