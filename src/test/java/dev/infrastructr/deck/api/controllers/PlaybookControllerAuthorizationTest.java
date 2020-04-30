package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreatePlaybookRequestBuilder.createPlaybookRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class PlaybookControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private PlaybookActions playbookActions;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        CreatePlaybookRequest request = createPlaybookRequest()
            .withProjectId(project.getId())
            .build();

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("playbook-create-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/playbooks", project.getId())
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
            .filter(getDocument("playbook-get-by-project-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/playbooks", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
