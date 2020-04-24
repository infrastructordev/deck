package dev.infrastructr.deck.data.entities;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "providers")
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    private String name;

    private String description;

    private String token;

    @Enumerated(EnumType.STRING)
    private ProviderType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Organization owner;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ProviderType getType() {
        return type;
    }

    public void setType(ProviderType type) {
        this.type = type;
    }

    public Organization getOwner() {
        return owner;
    }

    public void setOwner(Organization owner) {
        this.owner = owner;
    }
}
