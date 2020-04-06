package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.HostHeartbeat;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface HostHeartbeatRepository extends CrudRepository<HostHeartbeat, UUID> {

    Optional<HostHeartbeat> findByHostId(UUID hostId);
}
