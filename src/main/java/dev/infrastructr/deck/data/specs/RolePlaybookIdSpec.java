package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Role;

import java.util.UUID;

public class RolePlaybookIdSpec extends IdSpec<Role> {

    public RolePlaybookIdSpec(UUID playbookId){
        super(playbookId, "playbook");
    }
}
