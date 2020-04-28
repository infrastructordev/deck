package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.requests.CreateInventoryRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateInventoryRequestBuilder.createInventoryRequest;
import static io.restassured.RestAssured.given;

@Service
public class InventoryActions {

    public Inventory create(Cookie cookie, UUID projectId){
        CreateInventoryRequest request = createInventoryRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/inventories", projectId)
        .then()
            .extract()
            .body()
            .as(Inventory.class);
    }
}
