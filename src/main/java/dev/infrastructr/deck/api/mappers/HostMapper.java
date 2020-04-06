package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Host;
import org.mapstruct.*;

import java.util.List;

@Mapper
public abstract class HostMapper {

    public abstract Host map(dev.infrastructr.deck.data.entities.Host source);

    public abstract List<Host> map(List<dev.infrastructr.deck.data.entities.Host> sources);
}
