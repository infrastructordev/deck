package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class HostInitAuthorizationControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private HostInitActions hostInitActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidCreateInitForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Host host = hostActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("host-init-create-forbidden"))
            .cookie(anotherContext.getCookie())
        .when()
            .post("/hosts/{hostId}/init", host.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetInitForBadHostToken() {
        TestContext context = new TestContext();
        hostInitActions.create(context);
        Host host = context.getHosts().get(0);

        given(documentationSpec)
            .filter(getDocument("host-init-get-forbidden"))
        .when()
            .get("/hosts/{hostId}/init?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
    }
}
