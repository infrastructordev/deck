package dev.infrastructr.deck.data.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    private String name;

    private String description;

    @Type(type = "jsonb")
    @Column(name = "key_pair")
    private SshKeyPair keyPair;

    @Type(type = "jsonb")
    private Repository repository;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Organization owner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

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

    public Organization getOwner() {
        return owner;
    }

    public void setOwner(Organization owner) {
        this.owner = owner;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public SshKeyPair getKeyPair() {
        return keyPair;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public void setKeyPair(SshKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
