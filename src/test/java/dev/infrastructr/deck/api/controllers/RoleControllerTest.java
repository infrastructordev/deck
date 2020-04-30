package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.RoleActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.builders.CreateRoleRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class RoleControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private PlaybookActions playbookActions;

    @Autowired
    private RoleActions roleActions;

    @Test
    public void shouldCreate(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());
        CreateRoleRequest request = CreateRoleRequestBuilder.createRoleRequest()
            .withPlaybookId(playbook.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("playbook-create"))
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/playbooks/{playbookId}/roles", playbook.getId())
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
        Role role = roleActions.create(cookie, playbook.getId());

        given(documentationSpec)
            .filter(getDocument("role-get-by-playbook-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(role.getId().toString()))
            .body("content[0].name", is(role.getName()))
            .body("content[0].description", is(role.getDescription()));
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());
        Role role = roleActions.create(cookie, playbook.getId());

        given(documentationSpec)
            .filter(getDocument("role-get-filtered"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles?filter={filter}", playbook.getId(), role.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(role.getId().toString()))
            .body("content[0].name", is(role.getName()))
            .body("content[0].description", is(role.getDescription()));
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());
        Role role = roleActions.create(cookie, playbook.getId());
        Role anotherRole = roleActions.create(cookie, playbook.getId());
        String sort = "name,desc";
        Role expectedRole = role.getName().compareTo(anotherRole.getName()) > 0
            ? role
            : anotherRole;

        given(documentationSpec)
            .filter(getDocument("role-get-by-playbook-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/playbooks/{projectId}/roles?page=0&size=1&sort={sort}", playbook.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedRole.getId().toString()))
            .body("content[0].name", is(expectedRole.getName()))
            .body("content[0].description", is(expectedRole.getDescription()));
    }

    @Test
    public void shouldGetById(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Playbook playbook = playbookActions.create(cookie, project.getId());
        Role role = roleActions.create(cookie, playbook.getId());

        given(documentationSpec)
            .filter(getDocument("role-get-by-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/roles/{playbooksId}", role.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(role.getId().toString()))
            .body("name", is(role.getName()))
            .body("description", is(role.getDescription()));
    }
}
