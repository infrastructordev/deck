package dev.infrastructr.deck.git.services;

import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.git.models.Repository;

public interface GitRepositoryService {

    Repository create(Project project);
}
