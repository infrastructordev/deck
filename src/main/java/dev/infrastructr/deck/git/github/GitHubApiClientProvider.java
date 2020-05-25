package dev.infrastructr.deck.git.github;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.stereotype.Service;

@Service
public class GitHubApiClientProvider {

    public GitHub get(String token) {
        try {
            return new GitHubBuilder()
                .withOAuthToken(token)
                .build();
        } catch (Exception e){
            throw new RuntimeException("GitHub connection could not be created.", e);
        }
    }
}
