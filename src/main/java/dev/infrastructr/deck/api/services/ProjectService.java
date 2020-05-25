package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.ProjectMapper;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.data.specs.ProjectFilterSpec;
import dev.infrastructr.deck.data.specs.ProjectOwnerIdSpec;
import dev.infrastructr.deck.security.authorizers.ProjectAuthorizer;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectService {

    private final CurrentUserProvider currentUserProvider;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final ProjectAuthorizer projectAuthorizer;

    private final ProjectCreator projectCreator;

    public ProjectService(
        CurrentUserProvider currentUserProvider,
        ProjectRepository projectRepository,
        ProjectMapper projectMapper,
        ProjectAuthorizer projectAuthorizer,
        ProjectCreator projectCreator
    ){
        this.currentUserProvider = currentUserProvider;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectAuthorizer = projectAuthorizer;
        this.projectCreator = projectCreator;
    }

    public Project create(CreateProjectRequest createProjectRequest){
        return projectCreator.create(createProjectRequest);
    }

    public Page<Project> getAll(Pageable pageable, String filter){
        User user = currentUserProvider.getCurrentUser();
        return projectRepository
            .findAll(
                Specification
                    .where(new ProjectOwnerIdSpec(user.getOrganization().getId()))
                    .and(new ProjectFilterSpec(filter)),
                pageable
            )
            .map(projectMapper::map);
    }

    public Project getById(UUID id){
        projectAuthorizer.authorizeByProjectId(id);

        dev.infrastructr.deck.data.entities.Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);

        return projectMapper.map(project);
    }
}
