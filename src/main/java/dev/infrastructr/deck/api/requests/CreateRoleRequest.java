package dev.infrastructr.deck.api.requests;

import java.util.UUID;

public class CreateRoleRequest {

    private UUID playbookId;

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getPlaybookId() {
        return playbookId;
    }

    public void setPlaybookId(UUID playbookId) {
        this.playbookId = playbookId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
