package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Provider;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {ReferenceMapper.class})
public interface ProviderMapper {

    Provider map(dev.infrastructr.deck.data.entities.Provider source);

    List<Provider> map(List<dev.infrastructr.deck.data.entities.Project> sources);
}
