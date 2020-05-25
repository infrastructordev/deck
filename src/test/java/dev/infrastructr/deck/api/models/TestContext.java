package dev.infrastructr.deck.api.models;

import dev.infrastructr.deck.api.entities.*;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.entities.Organization;
import io.restassured.http.Cookie;

import java.util.ArrayList;
import java.util.List;

public class TestContext {

    private User user;

    private Organization organization;

    private Cookie cookie;

    private final List<Provider> providers = new ArrayList<>();

    private final List<Project> projects = new ArrayList<>();

    private final List<Inventory> inventories = new ArrayList<>();

    private final List<Host> hosts = new ArrayList<>();

    private final List<HostInit> hostInits = new ArrayList<>();

    private final List<Playbook> playbooks = new ArrayList<>();

    private final List<Group> groups = new ArrayList<>();

    private final List<Role> roles = new ArrayList<>();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public List<HostInit> getHostInits() {
        return hostInits;
    }

    public List<Playbook> getPlaybooks() {
        return playbooks;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
