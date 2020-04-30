package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Playbook;
import dev.infrastructr.deck.data.entities.Role;
import dev.infrastructr.deck.data.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleAuthorizer {

    private final PlaybookAuthorizer playbookAuthorizer;

    private final RoleRepository roleRepository;

    public RoleAuthorizer(
        PlaybookAuthorizer playbookAuthorizer,
        RoleRepository roleRepository
    ){
        this.playbookAuthorizer = playbookAuthorizer;
        this.roleRepository = roleRepository;
    }

    public void authorizeByRoleId(UUID roleId){
        Role role = roleRepository.findById(roleId).orElseThrow(NotFoundException::new);
        Playbook playbook = role.getPlaybook();

        playbookAuthorizer.authorizeByPlaybookId(playbook.getId());
    }

    public void authorizeByPlaybookId(UUID playbookId){
        playbookAuthorizer.authorizeByPlaybookId(playbookId);
    }
}
