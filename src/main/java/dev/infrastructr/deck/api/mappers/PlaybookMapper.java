package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Playbook;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class PlaybookMapper {

    public abstract Playbook map(dev.infrastructr.deck.data.entities.Playbook source);

    public abstract List<Playbook> map(List<dev.infrastructr.deck.data.entities.Playbook> sources);
}
