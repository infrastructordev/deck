package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateInventoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateInventoryRequestBuilder.createInventoryRequest;
import static io.restassured.RestAssured.given;

@Service
public class InventoryActions {

    @Autowired
    private ProjectActions projectActions;

    public Inventory create(TestContext context){
        Project project = projectActions.create(context);
        return create(context, project.getId());
    }

    public Inventory create(TestContext context, UUID projectId){
        CreateInventoryRequest request = createInventoryRequest().build();

        Inventory inventory = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/inventories", projectId)
        .then()
            .extract()
            .body()
            .as(Inventory.class);

        context.getInventories().add(inventory);

        return inventory;
    }
}
