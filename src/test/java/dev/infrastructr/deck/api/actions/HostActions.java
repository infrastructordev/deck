package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreateHostRequestBuilder;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class HostActions {

    public Host create(Cookie cookie, UUID inventoryId){
        CreateHostRequest request = CreateHostRequestBuilder.createHostRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/inventories/{inventoryId}/hosts", inventoryId)
        .then()
            .extract()
            .body()
            .as(Host.class);
    }
}
