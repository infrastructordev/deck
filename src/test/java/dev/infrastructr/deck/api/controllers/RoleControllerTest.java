package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.PlaybookActions;
import dev.infrastructr.deck.api.actions.RoleActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.builders.CreateRoleRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class RoleControllerTest extends WebTestBase {

    @Autowired
    private PlaybookActions playbookActions;

    @Autowired
    private RoleActions roleActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);
        CreateRoleRequest request = CreateRoleRequestBuilder.createRoleRequest()
            .withPlaybookId(playbook.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("playbook-create"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetByPlaybookId(){
        TestContext context = new TestContext();
        Role role = roleActions.create(context);

        given(documentationSpec)
            .filter(getDocument("role-get-by-playbook-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles", context.getPlaybooks().get(0).getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(role.getId().toString()))
            .body("content[0].name", is(role.getName()))
            .body("content[0].description", is(role.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        TestContext context = new TestContext();
        Role role = roleActions.create(context);

        given(documentationSpec)
            .filter(getDocument("role-get-filtered"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles?filter={filter}", context.getPlaybooks().get(0).getId(), role.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(role.getId().toString()))
            .body("content[0].name", is(role.getName()))
            .body("content[0].description", is(role.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);
        Role role = roleActions.create(context, playbook.getId());
        Role anotherRole = roleActions.create(context, playbook.getId());
        String sort = "name,desc";
        Role expectedRole = role.getName().compareTo(anotherRole.getName()) > 0
            ? role
            : anotherRole;

        given(documentationSpec)
            .filter(getDocument("role-get-by-playbook-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles?page=0&size=1&sort={sort}", playbook.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedRole.getId().toString()))
            .body("content[0].name", is(expectedRole.getName()))
            .body("content[0].description", is(expectedRole.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Role role = roleActions.create(context);

        given(documentationSpec)
            .filter(getDocument("role-get-by-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/roles/{roleId}", role.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(role.getId().toString()))
            .body("name", is(role.getName()))
            .body("description", is(role.getDescription()));

        contextCleaner.clean(context);
    }
}
