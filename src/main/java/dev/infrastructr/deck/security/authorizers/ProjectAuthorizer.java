package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.security.exceptions.ForbiddenException;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
public class ProjectAuthorizer {

    private final CurrentUserProvider currentUserProvider;

    private final ProjectRepository projectRepository;

    public ProjectAuthorizer(
        CurrentUserProvider currentUserProvider,
        ProjectRepository projectRepository
    ){
        this.currentUserProvider = currentUserProvider;
        this.projectRepository = projectRepository;
    }

    public void authorizeByProjectId(UUID projectId){
        Project project = projectRepository.findById(projectId).orElseThrow(NotFoundException::new);

        authorizeByProject(project);
    }

    public void authorizeByProject(Project project){
        User user = currentUserProvider.getCurrentUser();

        if(!isUserAndProjectFromSameOrganization(user, project)){
            throw new ForbiddenException(
                format("User {0} has no access to project {1}.", user.getId(), project.getId())
            );
        }
    }

    boolean isUserAndProjectFromSameOrganization(User user, Project project){
        return project.getOwner().getId().equals(user.getOrganization().getId());
    }
}
