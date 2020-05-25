package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.exceptions.UnprocessableEntityException;
import dev.infrastructr.deck.api.mappers.ProjectMapper;
import dev.infrastructr.deck.api.mappers.RepositoryMapper;
import dev.infrastructr.deck.api.mappers.SshKeyPairMapper;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.data.entities.Provider;
import dev.infrastructr.deck.data.entities.SshKeyPair;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.data.repositories.ProviderRepository;
import dev.infrastructr.deck.git.services.GitRepositoryService;
import dev.infrastructr.deck.git.services.GitRepositoryServiceProvider;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import dev.infrastructr.deck.ssh.sevices.SshKeyPairService;
import org.springframework.stereotype.Service;

@Service
public class ProjectCreator {

    private final CurrentUserProvider currentUserProvider;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final SshKeyPairService sshKeyPairService;

    private final SshKeyPairMapper sshKeyPairMapper;

    private final ProviderRepository providerRepository;

    private final GitRepositoryServiceProvider gitRepositoryServiceProvider;

    private final RepositoryMapper repositoryMapper;

    public ProjectCreator(
        CurrentUserProvider currentUserProvider,
        ProjectRepository projectRepository,
        ProjectMapper projectMapper,
        SshKeyPairService sshKeyPairService,
        SshKeyPairMapper sshKeyPairMapper,
        ProviderRepository providerRepository,
        GitRepositoryServiceProvider gitRepositoryServiceProvider,
        RepositoryMapper repositoryMapper

    ) {
        this.currentUserProvider = currentUserProvider;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.sshKeyPairService = sshKeyPairService;
        this.sshKeyPairMapper = sshKeyPairMapper;
        this.providerRepository = providerRepository;
        this.gitRepositoryServiceProvider = gitRepositoryServiceProvider;
        this.repositoryMapper = repositoryMapper;
    }

    public Project create(CreateProjectRequest createProjectRequest){
        User user = currentUserProvider.getCurrentUser();
        Provider provider = providerRepository.findById(createProjectRequest.getProviderId()).orElseThrow(UnprocessableEntityException::new);
        GitRepositoryService gitRepositoryService = gitRepositoryServiceProvider.get(provider.getType());

        dev.infrastructr.deck.data.entities.Project project = new dev.infrastructr.deck.data.entities.Project();
        project.setAuthor(user);
        project.setOwner(user.getOrganization());
        project.setName(createProjectRequest.getName());
        project.setDescription(createProjectRequest.getDescription());
        project.setKeyPair(createKeyPair());
        project.setProvider(provider);
        project.setRepository(repositoryMapper.map(gitRepositoryService.create(project)));

        return projectMapper.map(projectRepository.save(project));
    }

    private SshKeyPair createKeyPair(){
        return sshKeyPairMapper.map(sshKeyPairService.generate());
    }
}
