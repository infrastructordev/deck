package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;

@Service
public class HostActions {

    public Host create(Cookie cookie, UUID projectId){
        CreateHostRequest request = createProjectRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/hosts", projectId)
        .then()
            .extract()
            .body()
            .as(Host.class);
    }
}
