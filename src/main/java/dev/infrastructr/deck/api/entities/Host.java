package dev.infrastructr.deck.api.entities;

import java.util.UUID;

public class Host extends BaseEntity {

    private UUID id;

    private String name;

    private String initCommand;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitCommand() {
        return initCommand;
    }

    public void setInitCommand(String initCommand) {
        this.initCommand = initCommand;
    }
}
