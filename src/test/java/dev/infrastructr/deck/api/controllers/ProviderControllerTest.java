package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.ProviderActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.data.entities.Organization;
import dev.infrastructr.deck.data.entities.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateProviderRequestBuilder.createProviderRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class ProviderControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProviderActions providerActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        userActions.create(context);
        Organization organization = context.getOrganization();
        CreateProviderRequest request = createProviderRequest().build();

        given(documentationSpec)
            .filter(getDocument("provider-create"))
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/providers")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(notNullValue()))
            .body("name", is(request.getName()))
            .body("description", is(request.getDescription()))
            .body("type", is(request.getType()))
            .body("owner.id", is(organization.getId().toString()))
            .body("owner.name", is(organization.getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetAll(){
        TestContext context = new TestContext();
        Provider provider = providerActions.create(context);
        Organization organization = context.getOrganization();

        given(documentationSpec)
            .filter(getDocument("provider-get-all"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/providers")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(provider.getId().toString()))
            .body("content[0].name", is(provider.getName()))
            .body("content[0].description", is(provider.getDescription()))
            .body("content[0].type", is(provider.getType()))
            .body("content[0].owner.id", is(organization.getId().toString()))
            .body("content[0].owner.name", is(organization.getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetFiltered(){
        TestContext context = new TestContext();
        userActions.create(context);
        Organization organization = context.getOrganization();
        Provider provider = providerActions.create(context);
        providerActions.create(context);

        given(documentationSpec)
            .filter(getDocument("provider-get-filtered"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/providers?filter={filter}", provider.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(provider.getId().toString()))
            .body("content[0].name", is(provider.getName()))
            .body("content[0].description", is(provider.getDescription()))
            .body("content[0].type", is(provider.getType()))
            .body("content[0].owner.id", is(organization.getId().toString()))
            .body("content[0].owner.name", is(organization.getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSorted(){
        TestContext context = new TestContext();
        userActions.create(context);
        Organization organization = context.getOrganization();
        Provider provider = providerActions.create(context);
        Provider anotherProvider = providerActions.create(context);
        String sort = "name,desc";
        Provider expectedProvider = provider.getName().compareTo(anotherProvider.getName()) > 0
            ? provider
            : anotherProvider;

        given(documentationSpec)
            .filter(getDocument("provider-get-all"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/providers?page=0&size=1&sort={sort}", sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedProvider.getId().toString()))
            .body("content[0].name", is(expectedProvider.getName()))
            .body("content[0].description", is(expectedProvider.getDescription()))
            .body("content[0].type", is(expectedProvider.getType()))
            .body("content[0].owner.id", is(organization.getId().toString()))
            .body("content[0].owner.name", is(organization.getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Provider provider = providerActions.create(context);
        Organization organization = context.getOrganization();

        given(documentationSpec)
            .filter(getDocument("provider-get-by-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/providers/{providerId}", provider.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(provider.getId().toString()))
            .body("name", is(provider.getName()))
            .body("description", is(provider.getDescription()))
            .body("type", is(provider.getType()))
            .body("owner.id", is(organization.getId().toString()))
            .body("owner.name", is(organization.getName()));

        contextCleaner.clean(context);
    }
}
