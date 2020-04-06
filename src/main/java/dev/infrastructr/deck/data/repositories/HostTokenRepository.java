package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.HostToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface HostTokenRepository extends CrudRepository<HostToken, UUID> {

    Optional<HostToken> findByHostId(UUID hostId);
}
