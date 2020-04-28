package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createHostRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class InventoryControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Test
    public void shouldCreate(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        CreateHostRequest request = createHostRequest()
            .withInventoryId(project.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("inventory-create"))
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/inventories", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(notNullValue()))
            .body("name", is(request.getName()))
            .body("description", is(request.getDescription()));
    }

    @Test
    public void shouldGetByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-project-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/inventories", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(inventory.getId().toString()))
            .body("content[0].name", is(inventory.getName()))
            .body("content[0].description", is(inventory.getDescription()));
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("inventory-get-filtered"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/inventories?filter={filter}", project.getId(), inventory.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(inventory.getId().toString()))
            .body("content[0].name", is(inventory.getName()))
            .body("content[0].description", is(inventory.getDescription()));
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Inventory anotherInventory = inventoryActions.create(cookie, project.getId());
        String sort = "name,desc";
        Inventory expectedInventory = inventory.getName().compareTo(anotherInventory.getName()) > 0
            ? inventory
            : anotherInventory;

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-project-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/inventories?page=0&size=1&sort={sort}", project.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedInventory.getId().toString()))
            .body("content[0].name", is(expectedInventory.getName()))
            .body("content[0].description", is(expectedInventory.getDescription()));
    }

    @Test
    public void shouldGetById(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/inventories/{inventoryId}", inventory.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(inventory.getId().toString()))
            .body("name", is(inventory.getName()))
            .body("description", is(inventory.getDescription()));
    }
}
