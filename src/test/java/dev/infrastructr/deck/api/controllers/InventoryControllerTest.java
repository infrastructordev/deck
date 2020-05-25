package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.InventoryActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createHostRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class InventoryControllerTest extends WebTestBase {

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        CreateHostRequest request = createHostRequest()
            .withInventoryId(project.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("inventory-create"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetByProjectId(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);
        Project project = context.getProjects().get(0);

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-project-id"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetFilteredByProjectId(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);
        Project project = context.getProjects().get(0);

        given(documentationSpec)
            .filter(getDocument("inventory-get-filtered"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        Inventory inventory = inventoryActions.create(context, project.getId());
        Inventory anotherInventory = inventoryActions.create(context, project.getId());
        String sort = "name,desc";
        Inventory expectedInventory = inventory.getName().compareTo(anotherInventory.getName()) > 0
            ? inventory
            : anotherInventory;

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-project-id"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Inventory inventory = inventoryActions.create(context);

        given(documentationSpec)
            .filter(getDocument("inventory-get-by-id"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }
}
