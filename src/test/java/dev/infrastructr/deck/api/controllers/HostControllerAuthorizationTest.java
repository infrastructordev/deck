package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.HostActions;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createHostRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.*;

public class HostControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(new TestContext());
        CreateHostRequest request = createHostRequest()
            .withInventoryId(inventory.getId())
            .build();

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("host-create-forbidden"))
            .cookie(anotherContext.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/hosts", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByProjectIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("host-get-by-project-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/hosts", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Host host = hostActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("host-get-by-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/hosts/{hostId}", host.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }
}
