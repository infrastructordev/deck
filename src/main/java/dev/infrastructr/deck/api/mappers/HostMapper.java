package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Host;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface HostMapper {

    Host map(dev.infrastructr.deck.data.entities.Host source);

    List<Host> map(List<dev.infrastructr.deck.data.entities.Host> sources);
}
