package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Inventory;
import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.data.repositories.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryAuthorizer {

    private final ProjectAuthorizer projectAuthorizer;

    private final InventoryRepository inventoryRepository;

    public InventoryAuthorizer(
        ProjectAuthorizer projectAuthorizer,
        InventoryRepository inventoryRepository
    ){
        this.projectAuthorizer = projectAuthorizer;
        this.inventoryRepository = inventoryRepository;
    }

    public void authorizeByInventoryId(UUID hostId){
        Inventory inventory = inventoryRepository.findById(hostId).orElseThrow(NotFoundException::new);
        Project project = inventory.getProject();

        projectAuthorizer.authorizeByProject(project);
    }

    public void authorizeByProjectId(UUID projectId){
        projectAuthorizer.authorizeByProjectId(projectId);
    }
}
