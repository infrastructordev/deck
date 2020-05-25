package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.builders.CreatePlaybookRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class PlaybookControllerTest extends WebTestBase {

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private PlaybookActions playbookActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        CreatePlaybookRequest request = CreatePlaybookRequestBuilder.createPlaybookRequest()
            .withProjectId(project.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("playbook-create"))
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/playbooks", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(notNullValue()))
            .body("name", is(request.getName()))
            .body("description", is(request.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetByProjectId(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        Playbook playbook = playbookActions.create(context, project.getId());

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-project-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/playbooks", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(playbook.getId().toString()))
            .body("content[0].name", is(playbook.getName()))
            .body("content[0].description", is(playbook.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        Playbook playbook = playbookActions.create(context, project.getId());

        given(documentationSpec)
            .filter(getDocument("playbook-get-filtered"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/playbooks?filter={filter}", project.getId(), playbook.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(playbook.getId().toString()))
            .body("content[0].name", is(playbook.getName()))
            .body("content[0].description", is(playbook.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        Playbook playbook = playbookActions.create(context, project.getId());
        Playbook anotherPlaybook = playbookActions.create(context, project.getId());
        String sort = "name,desc";
        Playbook expectedPlaybook = playbook.getName().compareTo(anotherPlaybook.getName()) > 0
            ? playbook
            : anotherPlaybook;

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-project-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/playbooks?page=0&size=1&sort={sort}", project.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedPlaybook.getId().toString()))
            .body("content[0].name", is(expectedPlaybook.getName()))
            .body("content[0].description", is(expectedPlaybook.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbooksId}", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(playbook.getId().toString()))
            .body("name", is(playbook.getName()))
            .body("description", is(playbook.getDescription()));

        contextCleaner.clean(context);
    }
}
