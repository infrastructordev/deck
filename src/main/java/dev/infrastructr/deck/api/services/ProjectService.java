package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.ProjectMapper;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.security.authorizers.ProjectAuthorizer;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectService {

    private final CurrentUserProvider currentUserProvider;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final ProjectAuthorizer projectAuthorizer;

    public ProjectService(
        CurrentUserProvider currentUserProvider,
        ProjectRepository projectRepository,
        ProjectMapper projectMapper,
        ProjectAuthorizer projectAuthorizer
    ){
        this.currentUserProvider = currentUserProvider;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectAuthorizer = projectAuthorizer;
    }

    public Project create(CreateProjectRequest createProjectRequest){
        User user = currentUserProvider.getCurrentUser();

        dev.infrastructr.deck.data.entities.Project project = new dev.infrastructr.deck.data.entities.Project();
        project.setAuthor(user);
        project.setOwner(user.getOrganization());
        project.setName(createProjectRequest.getName());

        return projectMapper.map(projectRepository.save(project));
    }

    public Page<Project> getAll(Pageable pageable){
        User user = currentUserProvider.getCurrentUser();
        return projectRepository
            .findByOwnerId(pageable, user.getOrganization().getId())
            .map(projectMapper::map);
    }

    public Project getById(UUID id){
        projectAuthorizer.authorizeByProjectId(id);

        dev.infrastructr.deck.data.entities.Project project = projectRepository.findById(id).orElseThrow(NotFoundException::new);

        return projectMapper.map(project);
    }

}
