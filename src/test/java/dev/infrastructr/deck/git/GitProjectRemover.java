package dev.infrastructr.deck.git;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.data.entities.ProviderType;
import dev.infrastructr.deck.data.repositories.ProviderRepository;
import dev.infrastructr.deck.git.github.GitHubProjectRemover;
import dev.infrastructr.deck.git.services.GitRepositoryNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GitProjectRemover {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private GitRepositoryNameGenerator repositoryNameGenerator;

    @Autowired
    private GitHubProjectRemover gitHubProjectRemover;

    public void delete(TestContext context) {
        for (Provider provider : context.getProviders()) {
            String token = providerRepository.findById(provider.getId()).orElseThrow(NotFoundException::new).getToken();
            String namespace = provider.getNamespace();

            for (Project project : context.getProjects()) {
                String repository = repositoryNameGenerator.generate(project.getName());
                if (provider.getType().equals(ProviderType.GITHUB.name())) {
                    gitHubProjectRemover.remove(namespace, repository, token);
                }
            }
        }
    }
}
