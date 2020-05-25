package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.data.entities.Repository;
import org.mapstruct.Mapper;

@Mapper
public interface RepositoryMapper {

    Repository map(dev.infrastructr.deck.git.models.Repository source);
}
