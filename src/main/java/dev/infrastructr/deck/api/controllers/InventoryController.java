package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.requests.CreateInventoryRequest;
import dev.infrastructr.deck.api.services.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @PostMapping("/projects/{projectId}/inventories")
    public @ResponseBody Inventory create(@PathVariable("projectId") UUID projectId, @RequestBody CreateInventoryRequest request) {
        request.setProjectId(projectId);
        return inventoryService.create(request);
    }

    @GetMapping("/projects/{projectId}/inventories")
    public @ResponseBody Page<Inventory> getAll(
        Pageable pageable,
        @PathVariable("projectId") UUID projectId
    ){
        return inventoryService.getByProjectId(pageable, projectId);
    }

    @GetMapping("/inventories/{inventoryId}")
    public @ResponseBody Inventory getById(@PathVariable("inventoryId") UUID inventoryId){
        return inventoryService.getById(inventoryId);
    }
}
