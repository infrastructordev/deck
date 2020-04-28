package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.HostMapper;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.data.repositories.HostRepository;
import dev.infrastructr.deck.data.repositories.InventoryRepository;
import dev.infrastructr.deck.data.specs.HostFilterSpec;
import dev.infrastructr.deck.data.specs.HostInventoryIdSpec;
import dev.infrastructr.deck.security.authorizers.HostAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HostService {

    private final HostRepository hostRepository;

    private final InventoryRepository inventoryRepository;

    private final HostMapper hostMapper;

    private final HostAuthorizer hostAuthorizer;

    public HostService(
        HostRepository hostRepository,
        HostMapper hostMapper,
        HostAuthorizer hostAuthorizer,
        InventoryRepository inventoryRepository
    ){
        this.hostRepository = hostRepository;
        this.hostMapper = hostMapper;
        this.hostAuthorizer = hostAuthorizer;
        this.inventoryRepository = inventoryRepository;
    }

    public Host create(CreateHostRequest createHostRequest){
        hostAuthorizer.authorizeByInventoryId(createHostRequest.getInventoryId());

        dev.infrastructr.deck.data.entities.Inventory inventory = inventoryRepository.findById(createHostRequest.getInventoryId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Host host = new dev.infrastructr.deck.data.entities.Host();
        host.setInventory(inventory);
        host.setName(createHostRequest.getName());
        host.setDescription(createHostRequest.getDescription());

        return hostMapper.map(hostRepository.save(host));
    }

    public Host getById(UUID id){
        hostAuthorizer.authorizeByHostId(id);

        dev.infrastructr.deck.data.entities.Host host = hostRepository.findById(id).orElseThrow(NotFoundException::new);

        return hostMapper.map(host);
    }

    public Page<Host> getByInventoryId(Pageable pageable, String filter, UUID inventoryId){
        hostAuthorizer.authorizeByInventoryId(inventoryId);

        return hostRepository
            .findAll(
                Specification
                    .where(new HostInventoryIdSpec(inventoryId))
                    .and(new HostFilterSpec(filter)),
                pageable
            )
            .map(hostMapper::map);
    }
}
