package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.api.services.HostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class HostController {

    private HostService hostService;

    public HostController(HostService hostService){
        this.hostService = hostService;
    }

    @PostMapping("/projects/{projectId}/hosts")
    public @ResponseBody Host create(@PathVariable("projectId") UUID projectId, @RequestBody CreateHostRequest request) {
        request.setProjectId(projectId);
        return hostService.create(request);
    }

    @GetMapping("/projects/{projectId}/hosts")
    public @ResponseBody List<Host> getAll(@PathVariable("projectId") UUID projectId){
        return hostService.getByProjectId(projectId);
    }

    @GetMapping("/hosts/{hostId}")
    public @ResponseBody Host getById(@PathVariable("hostId") UUID hostId){
        return hostService.getById(hostId);
    }
}
