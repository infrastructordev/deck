package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.GroupActions;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import dev.infrastructr.deck.data.entities.Group;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
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
    private ProjectActions projectActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private GroupActions groupActions;

    @Test
    public void shouldForbidCreateForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        CreateGroupRequest request = createGroupRequest()
            .withInventoryId(inventory.getId())
            .build();

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("groups-create-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{projectId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetByInventoryIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("group-get-by-inventory-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }

    @Test
    public void shouldForbidGetByIdForUserFromDifferentOrganization(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Group group = groupActions.create(cookie, inventory.getId());

        User userFromDifferentOrganization = userActions.create();

        given(documentationSpec)
            .filter(getDocument("group-get-by-id-forbidden"))
            .cookie(userActions.authenticate(userFromDifferentOrganization))
            .contentType("application/json")
        .when()
            .get("/groups/{groupId}", group.getId())
        .then()
            .assertThat()
            .statusCode(is(FORBIDDEN.value()));
    }
}
