package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.builders.CreatePlaybookRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class PlaybookControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private PlaybookActions playbookActions;

    @Test
    public void shouldCreate(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        CreatePlaybookRequest request = CreatePlaybookRequestBuilder.createPlaybookRequest()
            .withProjectId(project.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("playbook-create"))
            .cookie(cookie)
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
    }

    @Test
    public void shouldGetByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-project-id"))
            .cookie(userActions.authenticate(user))
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
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("playbook-get-filtered"))
            .cookie(userActions.authenticate(user))
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
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());
        Playbook anotherPlaybook = playbookActions.create(cookie, project.getId());
        String sort = "name,desc";
        Playbook expectedPlaybook = playbook.getName().compareTo(anotherPlaybook.getName()) > 0
            ? playbook
            : anotherPlaybook;

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-project-id"))
            .cookie(userActions.authenticate(user))
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
    }

    @Test
    public void shouldGetById(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("playbook-get-by-id"))
            .cookie(userActions.authenticate(user))
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
    }
}
