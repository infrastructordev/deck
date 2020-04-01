package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends CrudRepository<Project, UUID> {

    List<Project> findByOwnerId(UUID ownerId);
}
