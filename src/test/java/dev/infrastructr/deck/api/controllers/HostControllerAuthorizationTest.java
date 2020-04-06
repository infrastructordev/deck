package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.HostActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.*;

public class HostControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private HostActions hostActions;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        CreateHostRequest request = createProjectRequest()
            .withProjectId(project.getId())
            .build();

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("host-create-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/hosts", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetByProjectIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("host-get-by-project-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/hosts", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("host-get-by-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/hosts/{hostId}", host.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
