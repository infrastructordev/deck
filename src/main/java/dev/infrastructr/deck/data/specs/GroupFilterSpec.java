package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Group;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GroupFilterSpec extends FilterSpec<Group> {

    public GroupFilterSpec(String filter) {
        super(filter);
    }

    @Override
    public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return builder.or(
            builder.like(builder.lower(root.get("name")), pattern, ESCAPE_CHAR),
            builder.like(builder.lower(root.get("description")), pattern, ESCAPE_CHAR)
        );
    }
}
