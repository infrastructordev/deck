package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Reference;
import org.mapstruct.Mapper;

@Mapper
public interface ReferenceMapper {
    Reference map(dev.infrastructr.deck.data.entities.User source);

    Reference map(dev.infrastructr.deck.data.entities.Organization source);
}

