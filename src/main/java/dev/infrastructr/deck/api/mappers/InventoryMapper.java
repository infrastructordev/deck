package dev.infrastructr.deck.api.mappers;

import dev.infrastructr.deck.api.entities.Inventory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class InventoryMapper {

    public abstract Inventory map(dev.infrastructr.deck.data.entities.Inventory source);

    public abstract List<Inventory> map(List<dev.infrastructr.deck.data.entities.Inventory> sources);
}
