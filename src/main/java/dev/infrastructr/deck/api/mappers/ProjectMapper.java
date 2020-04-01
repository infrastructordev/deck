package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class})
public interface ProjectMapper {

    Project map(dev.infrastructr.deck.data.entities.Project source);

    List<Project> map(List<dev.infrastructr.deck.data.entities.Project> sources);
}
