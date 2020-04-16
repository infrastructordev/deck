package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Project;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

public class ProjectOwnerIdSpec implements Specification<Project> {

    private static final long serialVersionUID=1L;

    private UUID ownerId;

    public ProjectOwnerIdSpec(UUID ownerId){
        this.ownerId = ownerId;
    }
    @Override
    public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder builder){
        return builder.equal(root.get("owner").get("id"), ownerId);
    }
}
