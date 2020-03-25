package dev.infrastructr.deck.security;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.data.entities.User;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTest extends WebTestBase {

    @Test
    public void shouldAuthenticateWithProperCredentials(){
        User user = user(PASSWORD);

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
            .cookie(cookie, not(emptyOrNullString()));
    }

    @Test
    public void shouldNotAuthenticateWithBadPassword(){
        User user = user(PASSWORD);

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
            .cookie(cookie, emptyOrNullString());
    }

    @Test
    public void shouldNotAuthenticateDisabled(){
        User user = user(PASSWORD, ACCOUNT_DISABLED);

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
            .cookie(cookie, emptyOrNullString());
    }

    @Test
    public void shouldLogout(){
        User user = user(PASSWORD);

        given(documentationSpec)
            .filter(getDocument("user-logout"))
            .auth().none()
            .cookie(authenticate(user))
        .when()
            .post("/users/logout")
        .then()
            .statusCode(is(OK.value()))
            .and()
            .cookie(cookie, emptyOrNullString());
    }
}
