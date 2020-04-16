package dev.infrastructr.deck.api.entities;

import java.util.UUID;

public class Project extends BaseEntity {

    private UUID id;

    private String name;

    private String description;

    private Reference owner;

    private Reference author;

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

    public Reference getOwner() {
        return owner;
    }

    public void setOwner(Reference owner) {
        this.owner = owner;
    }

    public Reference getAuthor() {
        return author;
    }

    public void setAuthor(Reference author) {
        this.author = author;
    }
}
