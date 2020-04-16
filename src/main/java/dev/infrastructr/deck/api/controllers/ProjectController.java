package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import dev.infrastructr.deck.api.services.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping
    public @ResponseBody Project create(@RequestBody CreateProjectRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public @ResponseBody Page<Project> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter
    ){
        return projectService.getAll(pageable, filter);
    }

    @GetMapping("/{projectId}")
    public @ResponseBody Project getById(@PathVariable("projectId") UUID projectId){
        return projectService.getById(projectId);
    }
}
