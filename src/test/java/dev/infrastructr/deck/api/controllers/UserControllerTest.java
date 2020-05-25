package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.data.entities.Organization;
import dev.infrastructr.deck.data.entities.UserRole;
import dev.infrastructr.deck.data.entities.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

public class UserControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldReturn(){
        TestContext context = new TestContext();
        User user = userActions.create(context);
        Organization organization = context.getOrganization();

        given(documentationSpec)
            .filter(getDocument("user-get-me"))
            .auth().none()
            .cookie(context.getCookie())
        .when()
            .get("/users/me")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(user.getId().toString()))
            .body("name", is(user.getName()))
            .body("organization.name", is(organization.getName()))
            .body("organization.id", is(organization.getId().toString()))
            .body("roles", is(
                user.getRoles()
                    .stream()
                    .map(UserRole::name)
                    .collect(Collectors.toList())));

        contextCleaner.clean(context);
    }
}
