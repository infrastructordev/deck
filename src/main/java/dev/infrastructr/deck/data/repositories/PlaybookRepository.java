package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Playbook;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PlaybookRepository extends CrudRepository<Playbook, UUID>, JpaSpecificationExecutor<Playbook> {
}
