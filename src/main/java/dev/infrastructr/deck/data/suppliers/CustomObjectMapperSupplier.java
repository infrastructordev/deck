package dev.infrastructr.deck.data.suppliers;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class CustomObjectMapperSupplier implements com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier {

    private static final ObjectMapper MAPPER;
    
    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
        MAPPER.configure(WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
    @Override
    public ObjectMapper get() {
        return MAPPER;
    }
}
