package dev.infrastructr.deck.api.entities;

import java.util.UUID;

public class Provider extends BaseEntity {

    private UUID id;

    private String name;

    private String description;

    private String type;

    private String namespace;

    private Reference owner;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Reference getOwner() {
        return owner;
    }

    public void setOwner(Reference owner) {
        this.owner = owner;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
