package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.PlaybookMapper;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import dev.infrastructr.deck.data.repositories.PlaybookRepository;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.data.specs.PlaybookFilterSpec;
import dev.infrastructr.deck.data.specs.PlaybookProjectIdSpec;
import dev.infrastructr.deck.security.authorizers.PlaybookAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlaybookService {

    private final PlaybookRepository playbookRepository;

    private final ProjectRepository projectRepository;

    private final PlaybookMapper playbookMapper;

    private final PlaybookAuthorizer playbookAuthorizer;

    public PlaybookService(
        PlaybookRepository playbookRepository,
        PlaybookMapper playbookMapper,
        PlaybookAuthorizer playbookAuthorizer,
        ProjectRepository projectRepository
    ){
        this.playbookRepository = playbookRepository;
        this.playbookMapper = playbookMapper;
        this.playbookAuthorizer = playbookAuthorizer;
        this.projectRepository = projectRepository;
    }

    public Playbook create(CreatePlaybookRequest createInventoryRequest){
        playbookAuthorizer.authorizeByProjectId(createInventoryRequest.getProjectId());

        dev.infrastructr.deck.data.entities.Project project = projectRepository.findById(createInventoryRequest.getProjectId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Playbook playbook = new dev.infrastructr.deck.data.entities.Playbook();
        playbook.setProject(project);
        playbook.setName(createInventoryRequest.getName());
        playbook.setDescription(createInventoryRequest.getDescription());

        return playbookMapper.map(playbookRepository.save(playbook));
    }

    public Playbook getById(UUID id){
        playbookAuthorizer.authorizeByPlaybookId(id);

        dev.infrastructr.deck.data.entities.Playbook playbook = playbookRepository.findById(id).orElseThrow(NotFoundException::new);

        return playbookMapper.map(playbook);
    }

    public Page<Playbook> getByProjectId(Pageable pageable, String filter, UUID projectId){
        playbookAuthorizer.authorizeByProjectId(projectId);

        return playbookRepository
            .findAll(
                Specification
                    .where(new PlaybookProjectIdSpec(projectId))
                    .and(new PlaybookFilterSpec(filter)),
                pageable
            )
            .map(playbookMapper::map);
    }
}
