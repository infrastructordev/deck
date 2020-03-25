package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.User;
import org.mapstruct.Mapper;

@Mapper(uses = {OrganizationMapper.class})
public interface UserMapper {
    User map(dev.infrastructr.deck.data.entities.User user);
}
