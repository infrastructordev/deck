package dev.infrastructr.deck.api.requests;

import java.util.UUID;

public class CreateHostRequest {

    private UUID projectId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }
}
