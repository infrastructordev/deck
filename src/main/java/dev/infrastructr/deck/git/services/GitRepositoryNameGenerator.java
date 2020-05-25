package dev.infrastructr.deck.git.services;

import dev.infrastructr.deck.git.props.GitProps;
import org.springframework.stereotype.Service;

@Service
public class GitRepositoryNameGenerator {

    private final GitProps gitProps;

    public GitRepositoryNameGenerator(
        GitProps gitProps
    ){
        this.gitProps = gitProps;
    }

    public String generate(String projectName){
        return replaceNonAlphanumericWithDash(gitProps.getProjectNamePrefix() + projectName)
            .toLowerCase();
    }

    private String replaceNonAlphanumericWithDash(String value){
        return value.replaceAll("[^A-Za-z0-9\\-]", "-");
    }
}
