package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.api.services.HostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class HostController {

    private final HostService hostService;

    public HostController(HostService hostService){
        this.hostService = hostService;
    }

    @PostMapping("/inventories/{inventoryId}/hosts")
    public @ResponseBody Host create(@PathVariable("inventoryId") UUID inventoryId, @RequestBody CreateHostRequest request) {
        request.setInventoryId(inventoryId);
        return hostService.create(request);
    }

    @GetMapping("/inventories/{inventoryId}/hosts")
    public @ResponseBody Page<Host> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter,
        @PathVariable("inventoryId") UUID inventoryId
    ){
        return hostService.getByInventoryId(pageable, filter, inventoryId);
    }

    @GetMapping("/hosts/{hostId}")
    public @ResponseBody Host getById(@PathVariable("hostId") UUID hostId){
        return hostService.getById(hostId);
    }
}
