package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Playbook;
import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.data.repositories.PlaybookRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlaybookAuthorizer {

    private final ProjectAuthorizer projectAuthorizer;

    private final PlaybookRepository playbookRepository;

    public PlaybookAuthorizer(
        ProjectAuthorizer projectAuthorizer,
        PlaybookRepository playbookRepository
    ){
        this.projectAuthorizer = projectAuthorizer;
        this.playbookRepository = playbookRepository;
    }

    public void authorizeByPlaybookId(UUID playbookId){
        Playbook playbook = playbookRepository.findById(playbookId).orElseThrow(NotFoundException::new);
        Project project = playbook.getProject();

        projectAuthorizer.authorizeByProject(project);
    }

    public void authorizeByProjectId(UUID projectId){
        projectAuthorizer.authorizeByProjectId(projectId);
    }
}
