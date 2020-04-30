package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import dev.infrastructr.deck.api.services.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/playbooks/{playbookId}/roles")
    public @ResponseBody Role create(@PathVariable("playbookId") UUID playbookId, @RequestBody CreateRoleRequest request) {
        request.setPlaybookId(playbookId);
        return roleService.create(request);
    }

    @GetMapping("/playbooks/{playbookId}/roles")
    public @ResponseBody Page<Role> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter,
        @PathVariable("playbookId") UUID playbookId
    ){
        return roleService.getByPlaybookId(pageable, filter, playbookId);
    }

    @GetMapping("/roles/{roleId}")
    public @ResponseBody Role getById(@PathVariable("roleId") UUID roleId){
        return roleService.getById(roleId);
    }
}
