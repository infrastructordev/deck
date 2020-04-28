package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.requests.CreateGroupRequest;
import dev.infrastructr.deck.data.entities.Group;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateGroupRequestBuilder.createGroupRequest;
import static io.restassured.RestAssured.given;

@Service
public class GroupActions {

    public Group create(Cookie cookie, UUID inventoryId){
        CreateGroupRequest request = createGroupRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/groups", inventoryId)
        .then()
            .extract()
            .body()
            .as(Group.class);
    }
}
