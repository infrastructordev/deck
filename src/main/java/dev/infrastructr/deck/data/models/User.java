package dev.infrastructr.deck.data.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Entity;

import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "users")
public class User extends Model {

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String name;

    private String password;

    @ManyToOne(fetch = LAZY, cascade = DETACH)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Type(type = "jsonb")
    private List<Role> roles;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
