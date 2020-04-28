package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Provider;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ProviderFilterSpec extends FilterSpec<Provider> {

    public ProviderFilterSpec(String filter) {
        super(filter);
    }

    @Override
    public Predicate toPredicate(Root<Provider> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.or(
            builder.like(builder.lower(root.get("name")), pattern, ESCAPE_CHAR),
            builder.like(builder.lower(root.get("description")), pattern, ESCAPE_CHAR),
            builder.like(builder.lower(root.get("type").as(String.class)), pattern, ESCAPE_CHAR)
        );
    }
}
