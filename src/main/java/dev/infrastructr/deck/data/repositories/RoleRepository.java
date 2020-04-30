package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
}
