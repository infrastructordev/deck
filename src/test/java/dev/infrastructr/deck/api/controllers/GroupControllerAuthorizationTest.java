package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.GroupActions;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Group;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateGroupRequestBuilder.createGroupRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class GroupControllerAuthorizationTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private GroupActions groupActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);
        CreateGroupRequest request = createGroupRequest()
            .withInventoryId(inventory.getId())
            .build();

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("groups-create-forbidden"))
            .cookie(anotherContext.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{projectId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByInventoryIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("group-get-by-inventory-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        TestContext context = new TestContext();
        Group group = groupActions.create(context);

        TestContext anotherContext = new TestContext();
        userActions.create(anotherContext);

        given(documentationSpec)
            .filter(getDocument("group-get-by-id-forbidden"))
            .cookie(anotherContext.getCookie())
            .contentType("application/json")
        .when()
            .get("/groups/{groupId}", group.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));

        contextCleaner.clean(context);
        contextCleaner.clean(anotherContext);
    }
}
