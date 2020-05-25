package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreateHostRequestBuilder;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class HostActions {

    @Autowired
    private InventoryActions inventoryActions;

    public Host create(TestContext context){
        Inventory inventory = inventoryActions.create(context);
        return create(context, inventory.getId());
    }

    public Host create(TestContext context, UUID inventoryId){
        CreateHostRequest request = CreateHostRequestBuilder.createHostRequest().build();

        Host host = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/hosts", inventoryId)
        .then()
            .extract()
            .body()
            .as(Host.class);

        context.getHosts().add(host);

        return host;
    }
}
