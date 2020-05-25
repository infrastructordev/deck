package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateRoleRequestBuilder.createRoleRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class RoleControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private PlaybookActions playbookActions;

    @Autowired
    private RoleActions roleActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);
        CreateRoleRequest request = createRoleRequest()
            .withPlaybookId(playbook.getId())
            .build();

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("role-create-forbidden"))
            .cookie(anotherContext.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/playbooks/{playbookId}/roles", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByProjectIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Playbook playbook = playbookActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("role-get-by-playbook-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/playbooks/{playbookId}/roles", playbook.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Role role = roleActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("role-get-by-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/roles/{roleId}", role.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }
}
