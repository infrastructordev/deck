package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.RoleMapper;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import dev.infrastructr.deck.data.repositories.PlaybookRepository;
import dev.infrastructr.deck.data.repositories.RoleRepository;
import dev.infrastructr.deck.data.specs.RoleFilterSpec;
import dev.infrastructr.deck.data.specs.RolePlaybookIdSpec;
import dev.infrastructr.deck.security.authorizers.RoleAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PlaybookRepository playbookRepository;

    private final RoleMapper roleMapper;

    private final RoleAuthorizer roleAuthorizer;

    public RoleService(
        RoleRepository roleRepository,
        RoleMapper roleMapper,
        RoleAuthorizer roleAuthorizer,
        PlaybookRepository playbookRepository
    ){
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleAuthorizer = roleAuthorizer;
        this.playbookRepository = playbookRepository;
    }

    public Role create(CreateRoleRequest createRoleRequest){
        roleAuthorizer.authorizeByPlaybookId(createRoleRequest.getPlaybookId());

        dev.infrastructr.deck.data.entities.Playbook playbook = playbookRepository.findById(createRoleRequest.getPlaybookId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Role role = new dev.infrastructr.deck.data.entities.Role();
        role.setPlaybook(playbook);
        role.setName(createRoleRequest.getName());
        role.setDescription(createRoleRequest.getDescription());

        return roleMapper.map(roleRepository.save(role));
    }

    public Role getById(UUID roleId){
        roleAuthorizer.authorizeByRoleId(roleId);

        dev.infrastructr.deck.data.entities.Role role = roleRepository.findById(roleId).orElseThrow(NotFoundException::new);

        return roleMapper.map(role);
    }

    public Page<Role> getByPlaybookId(Pageable pageable, String filter, UUID playbookId){
        roleAuthorizer.authorizeByPlaybookId(playbookId);

        return roleRepository
            .findAll(
                Specification
                    .where(new RolePlaybookIdSpec(playbookId))
                    .and(new RoleFilterSpec(filter)),
                pageable
            )
            .map(roleMapper::map);
    }
}
