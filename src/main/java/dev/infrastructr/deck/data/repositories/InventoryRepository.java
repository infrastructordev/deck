package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Inventory;
import dev.infrastructr.deck.data.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InventoryRepository extends CrudRepository<Inventory, UUID>, JpaSpecificationExecutor<Project> {

    Page<Inventory> findByProjectId(Pageable pageable, UUID projectId);
}
