package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreateRoleRequestBuilder;
import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class RoleActions {

    public Role create(Cookie cookie, UUID playbookId){
        CreateRoleRequest request = CreateRoleRequestBuilder.createRoleRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/playbooks/{playbookId}/roles", playbookId)
        .then()
            .extract()
            .body()
            .as(Role.class);
    }
}
