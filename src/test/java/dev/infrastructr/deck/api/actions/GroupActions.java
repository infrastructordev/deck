package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Group;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateGroupRequestBuilder.createGroupRequest;
import static io.restassured.RestAssured.given;

@Service
public class GroupActions {

    @Autowired
    private InventoryActions inventoryActions;

    public Group create(TestContext context){
        Inventory inventory = inventoryActions.create(context);
        return create(context, inventory.getId());
    }

    public Group create(TestContext context, UUID inventoryId){
        CreateGroupRequest request = createGroupRequest().build();

        Group group = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/groups", inventoryId)
        .then()
            .extract()
            .body()
            .as(Group.class);

        context.getGroups().add(group);

        return group;
    }
}
