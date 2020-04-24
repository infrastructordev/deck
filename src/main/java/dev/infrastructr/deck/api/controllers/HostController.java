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

    @PostMapping("/projects/{projectId}/hosts")
    public @ResponseBody Host create(@PathVariable("projectId") UUID projectId, @RequestBody CreateHostRequest request) {
        request.setProjectId(projectId);
        return hostService.create(request);
    }

    @GetMapping("/projects/{projectId}/hosts")
    public @ResponseBody Page<Host> getAll(
        Pageable pageable,
        @PathVariable("projectId") UUID projectId
    ){
        return hostService.getByProjectId(pageable, projectId);
    }

    @GetMapping("/hosts/{hostId}")
    public @ResponseBody Host getById(@PathVariable("hostId") UUID hostId){
        return hostService.getById(hostId);
    }
}
