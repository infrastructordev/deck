package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.HostActions;
import dev.infrastructr.deck.api.actions.HostInitActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Project;
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
    private ProjectActions projectActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private HostInitActions hostInitActions;

    @Test
    public void shouldForbidCreateInitForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("host-init-create-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
        .when()
            .post("/hosts/{hostId}/init", host.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetInitForBadHostToken() {
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());
        hostInitActions.create(cookie, host.getId());

        given(documentationSpec)
            .filter(getDocument("host-init-get-forbidden"))
        .when()
            .get("/hosts/{hostId}/init?hostToken={hostToken}", host.getId(), UUID.randomUUID())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
