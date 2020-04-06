package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class ProjectControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Test
    public void shouldNotGetAllForUserFromDifferentOrganization(){
        User user = userActions.create();
        projectActions.create(userActions.authenticate(user));

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/projects")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("$", hasSize(0));
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("project-get-by-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
