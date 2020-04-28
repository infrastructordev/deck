package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Project;
import java.util.UUID;

public class ProjectOwnerIdSpec extends IdSpec<Project> {

    public ProjectOwnerIdSpec(UUID ownerId){
        super(ownerId, "owner");
    }
}
