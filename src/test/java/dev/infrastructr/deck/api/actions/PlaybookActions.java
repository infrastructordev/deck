package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreatePlaybookRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class PlaybookActions {

    public Playbook create(Cookie cookie, UUID projectId){
        CreatePlaybookRequest request = CreatePlaybookRequestBuilder.createPlaybookRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/playbooks", projectId)
        .then()
            .extract()
            .body()
            .as(Playbook.class);
    }
}
