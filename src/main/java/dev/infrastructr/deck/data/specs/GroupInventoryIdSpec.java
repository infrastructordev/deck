package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Group;

import java.util.UUID;

public class GroupInventoryIdSpec extends IdSpec<Group> {

    public GroupInventoryIdSpec(UUID inventoryId){
        super(inventoryId, "inventory");
    }
}
