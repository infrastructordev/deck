package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import dev.infrastructr.deck.api.services.PlaybookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class PlaybookController {
    private final PlaybookService playbookService;

    public PlaybookController(PlaybookService playbookService){
        this.playbookService = playbookService;
    }

    @PostMapping("/projects/{projectId}/playbooks")
    public @ResponseBody Playbook create(@PathVariable("projectId") UUID projectId, @RequestBody CreatePlaybookRequest request) {
        request.setProjectId(projectId);
        return playbookService.create(request);
    }

    @GetMapping("/projects/{projectId}/playbooks")
    public @ResponseBody Page<Playbook> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter,
        @PathVariable("projectId") UUID projectId
    ){
        return playbookService.getByProjectId(pageable, filter, projectId);
    }

    @GetMapping("/playbooks/{playbookId}")
    public @ResponseBody Playbook getById(@PathVariable("playbookId") UUID playbookId){
        return playbookService.getById(playbookId);
    }
}
