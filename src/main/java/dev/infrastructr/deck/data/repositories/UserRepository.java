package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @EntityGraph(attributePaths = {
        "organization"
    })
    Optional<User> findByNameIgnoreCase(String name);
}
