package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.InventoryMapper;
import dev.infrastructr.deck.api.requests.CreateInventoryRequest;
import dev.infrastructr.deck.data.repositories.InventoryRepository;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.security.authorizers.InventoryAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ProjectRepository projectRepository;

    private final InventoryMapper inventoryMapper;

    private final InventoryAuthorizer inventoryAuthorizer;

    public InventoryService(
        InventoryRepository inventoryRepository,
        InventoryMapper inventoryMapper,
        InventoryAuthorizer inventoryAuthorizer,
        ProjectRepository projectRepository
    ){
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
        this.inventoryAuthorizer = inventoryAuthorizer;
        this.projectRepository = projectRepository;
    }

    public Inventory create(CreateInventoryRequest createInventoryRequest){
        inventoryAuthorizer.authorizeByProjectId(createInventoryRequest.getProjectId());

        dev.infrastructr.deck.data.entities.Project project = projectRepository.findById(createInventoryRequest.getProjectId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Inventory inventory = new dev.infrastructr.deck.data.entities.Inventory();
        inventory.setProject(project);
        inventory.setName(createInventoryRequest.getName());
        inventory.setDescription(createInventoryRequest.getDescription());

        return inventoryMapper.map(inventoryRepository.save(inventory));
    }

    public Inventory getById(UUID id){
        inventoryAuthorizer.authorizeByInventoryId(id);

        dev.infrastructr.deck.data.entities.Inventory inventory = inventoryRepository.findById(id).orElseThrow(NotFoundException::new);

        return inventoryMapper.map(inventory);
    }

    public Page<Inventory> getByProjectId(Pageable pageable, UUID projectId){
        inventoryAuthorizer.authorizeByProjectId(projectId);

        return inventoryRepository
            .findByProjectId(pageable, projectId)
            .map(inventoryMapper::map);
    }
}
