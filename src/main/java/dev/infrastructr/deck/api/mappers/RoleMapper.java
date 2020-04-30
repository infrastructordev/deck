package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class RoleMapper {

    public abstract Role map(dev.infrastructr.deck.data.entities.Role source);

    public abstract List<Role> map(List<dev.infrastructr.deck.data.entities.Role> sources);
}
