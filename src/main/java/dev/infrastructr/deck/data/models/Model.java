package dev.infrastructr.deck.data.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Model {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        return reflectionEquals(this, other, "jacksonTypeName");
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this, "jacksonTypeName");
    }
}
