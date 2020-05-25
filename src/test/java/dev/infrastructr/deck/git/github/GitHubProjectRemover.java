package dev.infrastructr.deck.git.github;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

@Service
public class GitHubProjectRemover {

    private final GitHubApiClientProvider apiClientProvider;

    private final GitHubOrganizationProvider organizationProvider;

    public GitHubProjectRemover(
        GitHubApiClientProvider apiClientProvider,
        GitHubOrganizationProvider organizationProvider

    ){
        this.apiClientProvider = apiClientProvider;
        this.organizationProvider = organizationProvider;
    }

    public void remove(String namespace, String repository, String token) {
        GitHub apiClient = apiClientProvider.get(token);
        GHOrganization organization = organizationProvider.get(namespace, apiClient);
        remove(repository, organization);
    }

    private void remove(String repository, GHOrganization organization){
        try {
            GHRepository ghRepository = organization.getRepository(repository);
            if(ghRepository != null) {
                ghRepository.delete();
            }
        } catch(Exception e){
            throw new RuntimeException("Can not delete project.", e);
        }
    }
}
