package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Group;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GroupRepository extends CrudRepository<Group, UUID>, JpaSpecificationExecutor<Group> {
}
