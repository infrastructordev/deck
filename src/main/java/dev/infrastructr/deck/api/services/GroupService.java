package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Group;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.GroupMapper;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import dev.infrastructr.deck.data.repositories.GroupRepository;
import dev.infrastructr.deck.data.repositories.InventoryRepository;
import dev.infrastructr.deck.data.specs.GroupFilterSpec;
import dev.infrastructr.deck.data.specs.GroupInventoryIdSpec;
import dev.infrastructr.deck.security.authorizers.GroupAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final InventoryRepository inventoryRepository;

    private final GroupMapper groupMapper;

    private final GroupAuthorizer groupAuthorizer;

    public GroupService(
        GroupRepository groupRepository,
        GroupMapper groupMapper,
        GroupAuthorizer groupAuthorizer,
        InventoryRepository inventoryRepository
    ){
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.groupAuthorizer = groupAuthorizer;
        this.inventoryRepository = inventoryRepository;
    }

    public Group create(CreateGroupRequest createGroupRequest){
        groupAuthorizer.authorizeByInventoryId(createGroupRequest.getInventoryId());

        dev.infrastructr.deck.data.entities.Inventory inventory = inventoryRepository.findById(createGroupRequest.getInventoryId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Group group = new dev.infrastructr.deck.data.entities.Group();
        group.setInventory(inventory);
        group.setName(createGroupRequest.getName());
        group.setDescription(createGroupRequest.getDescription());

        return groupMapper.map(groupRepository.save(group));
    }

    public Group getById(UUID id){
        groupAuthorizer.authorizeByGroupId(id);

        dev.infrastructr.deck.data.entities.Group group = groupRepository.findById(id).orElseThrow(NotFoundException::new);

        return groupMapper.map(group);
    }

    public Page<Group> getByInventoryId(Pageable pageable, String filter, UUID inventoryId){
        groupAuthorizer.authorizeByInventoryId(inventoryId);

        return groupRepository
            .findAll(
                Specification
                    .where(new GroupInventoryIdSpec(inventoryId))
                    .and(new GroupFilterSpec(filter)),
                pageable
            )
            .map(groupMapper::map);
    }
}
