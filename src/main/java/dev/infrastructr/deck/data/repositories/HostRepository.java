package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Host;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface HostRepository extends CrudRepository<Host, UUID> {

    List<Host> findByProjectId(UUID projectId);

}
