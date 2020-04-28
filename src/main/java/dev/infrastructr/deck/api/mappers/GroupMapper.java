package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class GroupMapper {

    public abstract Group map(dev.infrastructr.deck.data.entities.Group source);

    public abstract List<Group> map(List<dev.infrastructr.deck.data.entities.Group> sources);
}
