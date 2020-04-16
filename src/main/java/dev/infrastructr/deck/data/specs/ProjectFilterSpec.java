package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Project;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProjectFilterSpec implements Specification<Project> {

    private static final long serialVersionUID = 1L;

    private static final char ESCAPE_CHAR = '\\';

    private String pattern;

    public ProjectFilterSpec(String filter) {
        this.pattern = toPattern(filter);
    }

    @Override
    public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.or(
            builder.like(builder.lower(root.get("name")), pattern, ESCAPE_CHAR),
            builder.like(builder.lower(root.get("description")), pattern, ESCAPE_CHAR)
        );
    }

    private String toPattern(String value){
        return "%" + escape(value.toLowerCase()) + "%";
    }

    private static String escape(String value) {
        return value
            .replace("\\", ESCAPE_CHAR + "\\")
            .replace("_", ESCAPE_CHAR + "_")
            .replace("%", ESCAPE_CHAR + "%");
    }
}
