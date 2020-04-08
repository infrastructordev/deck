package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface HostRepository extends CrudRepository<Host, UUID> {

    Page<Host> findByProjectId(Pageable pageable, UUID projectId);

}
