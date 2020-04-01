package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import dev.infrastructr.deck.api.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping
    public @ResponseBody Project create(@RequestBody CreateProjectRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public @ResponseBody List<Project> getAll(){
        return projectService.getAll();
    }

    @GetMapping("/{projectId}")
    public @ResponseBody Project getById(@PathVariable("projectId") UUID projectId){
        return projectService.getById(projectId);
    }
}
