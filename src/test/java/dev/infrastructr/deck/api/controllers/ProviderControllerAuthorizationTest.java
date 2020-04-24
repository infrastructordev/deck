package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.ProviderActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class ProviderControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProviderActions providerActions;

    @Test
    public void shouldNotGetAllForUserFromDifferentOrganization(){
        User user = userActions.create();
        providerActions.create(userActions.authenticate(user));

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/providers")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content.$", hasSize(0));
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Provider provider = providerActions.create(cookie);

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("provider-get-by-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/providers/{providerId}", provider.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
