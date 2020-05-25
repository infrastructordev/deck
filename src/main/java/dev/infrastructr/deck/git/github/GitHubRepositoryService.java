package dev.infrastructr.deck.git.github;

import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.git.services.GitRepositoryNameGenerator;
import dev.infrastructr.deck.git.services.GitRepositoryService;
import dev.infrastructr.deck.git.models.Repository;
import dev.infrastructr.deck.git.props.GitProps;
import org.kohsuke.github.*;
import org.springframework.stereotype.Service;

@Service
public class GitHubRepositoryService implements GitRepositoryService {

    private final GitProps gitProps;

    private final GitHubApiClientProvider apiClientProvider;

    private final GitHubOrganizationProvider organizationProvider;

    private final GitRepositoryNameGenerator repositoryNameGenerator;

    public GitHubRepositoryService(
        GitProps gitProps,
        GitHubApiClientProvider apiClientProvider,
        GitHubOrganizationProvider organizationProvider,
        GitRepositoryNameGenerator repositoryNameGenerator
    ){
        this.gitProps = gitProps;
        this.apiClientProvider = apiClientProvider;
        this.organizationProvider = organizationProvider;
        this.repositoryNameGenerator = repositoryNameGenerator;
    }

    @Override
    public Repository create(Project project) {
        GitHub apiClient = apiClientProvider.get(project.getProvider().getToken());

        GHOrganization organization = organizationProvider.get(project.getProvider().getNamespace(), apiClient);
        GHRepository ghRepository = createRepository(project, organization);

        createDeployKey(project, ghRepository);

        Repository repository = new Repository();
        repository.setName(ghRepository.getName());
        repository.setUrl(ghRepository.getUrl().toString());
        repository.setSshUrl(ghRepository.getSshUrl());

        return repository;
    }

    private GHDeployKey createDeployKey(Project project, GHRepository repository){
        try {
            return repository.addDeployKey(gitProps.getKeyName(), project.getKeyPair().getPublicKey());
        } catch (Exception e){
            throw new RuntimeException("Deployment key could not be created", e);
        }
    }

    private GHRepository createRepository(Project project, GHOrganization organization){
        try {
            String repositoryName = repositoryNameGenerator.generate(project.getName());
            GHCreateRepositoryBuilder createRepositoryBuilder = organization.createRepository(repositoryName);
            createRepositoryBuilder.description(project.getDescription());
            createRepositoryBuilder.private_(true);
            return createRepositoryBuilder.create();
        } catch (Exception e){
            throw new RuntimeException("GitHub repository could not be created.", e);
        }
    }
}
