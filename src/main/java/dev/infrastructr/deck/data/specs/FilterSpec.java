package dev.infrastructr.deck.data.specs;

import org.springframework.data.jpa.domain.Specification;

public abstract class FilterSpec<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;

    protected static final char ESCAPE_CHAR = '\\';

    protected String pattern;

    public FilterSpec(String filter){
        this.pattern = toPattern(filter);
    }

    protected String toPattern(String value){
        return "%" + escape(value.toLowerCase()) + "%";
    }

    protected static String escape(String value) {
        return value
            .replace("\\", ESCAPE_CHAR + "\\")
            .replace("_", ESCAPE_CHAR + "_")
            .replace("%", ESCAPE_CHAR + "%");
    }
}

