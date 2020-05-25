package dev.infrastructr.deck.security;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.data.entities.User;
import liquibase.pro.packaged.T;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.actions.UserActions.ACCOUNT_DISABLED;
import static dev.infrastructr.deck.api.actions.UserActions.PASSWORD;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Test
    public void shouldAuthenticateWithProperCredentials(){
        User user = userActions.create(new TestContext(), PASSWORD);

        given(documentationSpec)
            .filter(getDocument("user-login"))
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", PASSWORD)
        .when()
            .post("/users/login")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .cookie(userActions.getCookieName(), not(emptyOrNullString()));
    }

    @Test
    public void shouldNotAuthenticateWithBadPassword(){
        User user = userActions.create(new TestContext(), PASSWORD);

        given(documentationSpec)
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", "secret")
        .when()
            .post("/users/login")
        .then()
            .assertThat()
            .statusCode(is(UNAUTHORIZED.value()))
            .and()
            .cookie(userActions.getCookieName(), emptyOrNullString());
    }

    @Test
    public void shouldNotAuthenticateDisabled(){
        User user = userActions.create(new TestContext(), PASSWORD, ACCOUNT_DISABLED);

        given(documentationSpec)
            .filter(getDocument("user-login-unauthorized"))
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", "secret")
        .when()
            .post("/users/login")
        .then()
            .assertThat()
            .statusCode(is(UNAUTHORIZED.value()))
            .and()
            .cookie(userActions.getCookieName(), emptyOrNullString());
    }

    @Test
    public void shouldLogout(){
        TestContext context = new TestContext();
        userActions.create(context, PASSWORD);

        given(documentationSpec)
            .filter(getDocument("user-logout"))
            .auth().none()
            .cookie(context.getCookie())
        .when()
            .post("/users/logout")
        .then()
            .statusCode(is(OK.value()))
            .and()
            .cookie(userActions.getCookieName(), emptyOrNullString());
    }
}
