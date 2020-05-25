package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
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

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldNotGetAllForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        projectActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content.$", hasSize(0));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("project-get-by-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }
}
