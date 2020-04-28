package dev.infrastructr.deck.data.specs;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class IdSpec<T> implements Specification<T> {

    private static final long serialVersionUID=1L;

    private UUID id;

    private String name;

    public IdSpec(
        UUID id,
        String name
    ){
        this.id = id;
        this.name = name;
    }
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder){
        return builder.equal(root.get(name).get("id"), id);
    }
}
