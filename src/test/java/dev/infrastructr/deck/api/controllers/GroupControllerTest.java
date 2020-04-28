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
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class GroupControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private GroupActions groupActions;

    @Test
    public void shouldCreate(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        CreateGroupRequest request = createGroupRequest()
            .withInventoryId(inventory.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("group-create"))
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(notNullValue()))
            .body("name", is(request.getName()))
            .body("description", is(request.getDescription()));
    }

    @Test
    public void shouldGetByInventoryId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Group group = groupActions.create(cookie, inventory.getId());

        given(documentationSpec)
            .filter(getDocument("group-get-by-inventory-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/groups", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(group.getId().toString()))
            .body("content[0].name", is(group.getName()))
            .body("content[0].description", is(group.getDescription()));
    }

    @Test
    public void shouldGetFilteredByInventoryId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Group group = groupActions.create(cookie, inventory.getId());

        given(documentationSpec)
            .filter(getDocument("group-get-filtered"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/groups?filter={filter}", inventory.getId(), group.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(group.getId().toString()))
            .body("content[0].name", is(group.getName()))
            .body("content[0].description", is(group.getDescription()));
    }

    @Test
    public void shouldGetOnePerPageSortedByInventoryId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Group group = groupActions.create(cookie, inventory.getId());
        Group anotherGroup = groupActions.create(cookie, inventory.getId());
        String sort = "name,desc";
        Group expectedGroup = group.getName().compareTo(anotherGroup.getName()) > 0
            ? group
            : anotherGroup;

        given(documentationSpec)
            .filter(getDocument("group-get-by-inventory-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}/groups?page=0&size=1&sort={sort}", inventory.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedGroup.getId().toString()))
            .body("content[0].name", is(expectedGroup.getName()))
            .body("content[0].description", is(expectedGroup.getDescription()));
    }

    @Test
    public void shouldGetById(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Group group = groupActions.create(cookie, inventory.getId());

        given(documentationSpec)
            .filter(getDocument("group-get-by-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/groups/{inventoryId}", group.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(group.getId().toString()))
            .body("name", is(group.getName()))
            .body("description", is(group.getDescription()));
    }
}
