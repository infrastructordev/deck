package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Host;
import dev.infrastructr.deck.data.entities.Inventory;
import dev.infrastructr.deck.data.repositories.HostRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HostAuthorizer {

    private final InventoryAuthorizer inventoryAuthorizer;

    private final HostRepository hostRepository;

    public HostAuthorizer(
        InventoryAuthorizer inventoryAuthorizer,
        HostRepository hostRepository
    ){
        this.inventoryAuthorizer = inventoryAuthorizer;
        this.hostRepository = hostRepository;
    }

    public void authorizeByHostId(UUID hostId){
        Host host = hostRepository.findById(hostId).orElseThrow(NotFoundException::new);
        Inventory inventory = host.getInventory();

        inventoryAuthorizer.authorizeByInventoryId(inventory.getId());
    }

    public void authorizeByInventoryId(UUID projectId){
        inventoryAuthorizer.authorizeByInventoryId(projectId);
    }
}
