package dev.infrastructr.deck.git.services;

import dev.infrastructr.deck.api.exceptions.InternalServerError;
import dev.infrastructr.deck.data.entities.ProviderType;
import dev.infrastructr.deck.git.github.GitHubRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class GitRepositoryServiceProvider {

    private GitHubRepositoryService gitHubRepositoryService;

    public GitRepositoryServiceProvider(
        GitHubRepositoryService gitHubRepositoryService
    ){
        this.gitHubRepositoryService = gitHubRepositoryService;
    }

    public GitRepositoryService get(ProviderType type){
        if (type == ProviderType.GITHUB) {
            return gitHubRepositoryService;
        }
        throw new InternalServerError();
    }
}
