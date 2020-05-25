package dev.infrastructr.deck.git.github;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import static java.text.MessageFormat.format;

@Service
public class GitHubOrganizationProvider {

    public GHOrganization get(String namespace, GitHub apiClient){
        try {
            return apiClient.getOrganization(namespace);
        } catch (Exception e){
            throw new RuntimeException(
                format("Organization {0} could not be retrieved.", namespace),
                e
            );
        }
    }
}
