package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Project;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProjectRepository extends CrudRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
}
