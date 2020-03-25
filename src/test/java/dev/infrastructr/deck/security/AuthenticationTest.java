package dev.infrastructr.deck.security;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.data.models.Organization;
import dev.infrastructr.deck.data.models.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static dev.infrastructr.deck.data.builders.OrganizationBuilder.organization;
import static dev.infrastructr.deck.data.builders.UserBuilder.user;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationTest extends WebTestBase {

    @Value("${dev.infrastructr.deck.security.remember-me.cookie}")
    private String cookie;

    private static final String PASSWORD = "123456";

    private static final boolean ACCOUNT_ENABLED = true;

    private static final boolean ACCOUNT_DISABLED = false;

    @Test
    public void shouldAuthenticateWithProperCredentials(){
        User user = getUser(PASSWORD, ACCOUNT_ENABLED);

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
        User user = getUser(PASSWORD, ACCOUNT_ENABLED);

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
        User user = getUser(PASSWORD, ACCOUNT_DISABLED);

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
        given(documentationSpec)
            .filter(getDocument("user-logout"))
            .auth().none()
            .cookie(authenticate())
        .when()
            .post("/users/logout")
        .then()
            .statusCode(is(OK.value()))
            .and()
            .cookie(cookie, emptyOrNullString());
    }

    private Cookie authenticate(){
        User user = getUser(PASSWORD, ACCOUNT_DISABLED);

        return given()
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", PASSWORD)
            .when()
            .post("/users/login")
            .then()
            .extract()
            .detailedCookie(cookie);
    }

    private User getUser(String password, boolean enabled){
        Organization organization = entityManager.persist(organization().build());
        return entityManager.persist(user()
            .withOrganization(organization)
            .withEnabled(enabled)
            .withPassword(password)
            .build());
    }
}
