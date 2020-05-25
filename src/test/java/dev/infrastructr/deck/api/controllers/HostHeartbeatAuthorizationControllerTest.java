package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.models.TestContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class HostHeartbeatAuthorizationControllerTest extends WebTestBase {

    @Autowired
    private HostInitActions hostInitActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidGetHeartbeatForBadHostToken() {
        TestContext context = new TestContext();
        hostInitActions.create(context);
        Host host = context.getHosts().get(0);

        given(documentationSpec)
            .filter(getDocument("host-heartbeat-get-forbidden"))
        .when()
            .get("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldForbidUpdateHeartbeatForBadHostToken() {
        TestContext context = new TestContext();
        hostInitActions.create(context);
        Host host = context.getHosts().get(0);

        given(documentationSpec)
            .filter(getDocument("host-heartbeat-create-forbidden"))
            .body(singletonMap("foo", "bar"))
            .contentType("application/json")
        .when()
            .post("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
    }
}
