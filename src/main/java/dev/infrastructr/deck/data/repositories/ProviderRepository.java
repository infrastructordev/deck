package dev.infrastructr.deck.data.repositories;

import dev.infrastructr.deck.data.entities.Provider;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProviderRepository extends CrudRepository<Provider, UUID>, JpaSpecificationExecutor<Provider> {

}
