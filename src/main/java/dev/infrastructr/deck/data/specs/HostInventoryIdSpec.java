package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Host;

import java.util.UUID;

public class HostInventoryIdSpec extends IdSpec<Host> {

    public HostInventoryIdSpec(UUID inventoryId){
        super(inventoryId, "inventory");
    }
}
