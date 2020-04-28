package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Group;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import dev.infrastructr.deck.api.services.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService){
        this.groupService = groupService;
    }

    @PostMapping("/inventories/{inventoryId}/groups")
    public @ResponseBody Group create(@PathVariable("inventoryId") UUID inventoryId, @RequestBody CreateGroupRequest request) {
        request.setInventoryId(inventoryId);
        return groupService.create(request);
    }

    @GetMapping("/inventories/{inventoryId}/groups")
    public @ResponseBody Page<Group> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter,
        @PathVariable("inventoryId") UUID inventoryId
    ){
        return groupService.getByInventoryId(pageable, filter, inventoryId);
    }

    @GetMapping("/groups/{groupId}")
    public @ResponseBody Group getById(@PathVariable("groupId") UUID groupId){
        return groupService.getById(groupId);
    }
}
