package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.HostActions;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createHostRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class HostControllerTest extends WebTestBase {

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);
        CreateHostRequest request = createHostRequest()
            .withInventoryId(inventory.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("host-create"))
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/hosts", inventory.getId())
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
    public void shouldGetByInventoryId(){
        TestContext context = new TestContext();
        Host host = hostActions.create(context);
        Inventory inventory = context.getInventories().get(0);

        given(documentationSpec)
            .filter(getDocument("host-get-by-inventory-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/hosts", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(host.getId().toString()))
            .body("content[0].name", is(host.getName()))
            .body("content[0].description", is(host.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);
        Host host = hostActions.create(context, inventory.getId());
        Host anotherHost = hostActions.create(context, inventory.getId());
        String sort = "name,desc";
        Host expectedHost = host.getName().compareTo(anotherHost.getName()) > 0
            ? host
            : anotherHost;

        given(documentationSpec)
            .filter(getDocument("host-get-by-inventory-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/hosts?page=0&size=1&sort={sort}", inventory.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedHost.getId().toString()))
            .body("content[0].name", is(expectedHost.getName()))
            .body("content[0].description", is(expectedHost.getDescription()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Host host = hostActions.create(context);

        given(documentationSpec)
            .filter(getDocument("host-get-by-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/hosts/{hostId}", host.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(host.getId().toString()))
            .body("name", is(host.getName()))
            .body("description", is(host.getDescription()));

        contextCleaner.clean(context);
    }
}
