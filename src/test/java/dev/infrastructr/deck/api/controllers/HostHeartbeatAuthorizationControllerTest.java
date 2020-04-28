package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class HostHeartbeatAuthorizationControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private HostInitActions hostInitActions;

    @Test
    public void shouldForbidGetHeartbeatForBadHostToken() {
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Host host = hostActions.create(cookie, inventory.getId());
        hostInitActions.create(cookie, host.getId());

        given(documentationSpec)
            .filter(getDocument("host-heartbeat-get-forbidden"))
        .when()
            .get("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidUpdateHeartbeatForBadHostToken() {
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Host host = hostActions.create(cookie, inventory.getId());
        hostInitActions.create(cookie, host.getId());

        given(documentationSpec)
            .filter(getDocument("host-heartbeat-create-forbidden"))
            .body(singletonMap("foo", "bar"))
            .contentType("application/json")
        .when()
            .post("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
