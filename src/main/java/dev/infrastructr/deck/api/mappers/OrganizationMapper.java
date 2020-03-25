package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Organization;
import org.mapstruct.Mapper;

@Mapper
public interface OrganizationMapper {
    Organization map(dev.infrastructr.deck.data.entities.Organization source);
}
