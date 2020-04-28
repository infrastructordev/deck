package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Group;
import dev.infrastructr.deck.data.entities.Inventory;
import dev.infrastructr.deck.data.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupAuthorizer {

    private final InventoryAuthorizer inventoryAuthorizer;

    private final GroupRepository groupRepository;

    public GroupAuthorizer(
        InventoryAuthorizer inventoryAuthorizer,
        GroupRepository groupRepository
    ){
        this.inventoryAuthorizer = inventoryAuthorizer;
        this.groupRepository = groupRepository;
    }

    public void authorizeByGroupId(UUID groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(NotFoundException::new);
        Inventory inventory = group.getInventory();

        inventoryAuthorizer.authorizeByInventoryId(inventory.getId());
    }

    public void authorizeByInventoryId(UUID inventoryId){
        inventoryAuthorizer.authorizeByInventoryId(inventoryId);
    }
}
