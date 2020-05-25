package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
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

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        CreatePlaybookRequest request = createPlaybookRequest()
            .withProjectId(project.getId())
            .build();

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("playbook-create-forbidden"))
            .cookie(anotherContext.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/playbooks", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByProjectIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-project-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/playbooks", project.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }
}
