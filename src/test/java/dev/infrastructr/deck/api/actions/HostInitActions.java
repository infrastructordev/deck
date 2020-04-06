package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.HostInit;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class HostInitActions {

    public HostInit create(Cookie cookie, UUID hostId) {
        return given()
            .cookie(cookie)
        .when()
            .post("/hosts/{hostId}/init", hostId)
        .then()
            .extract()
            .body()
            .as(HostInit.class);
    }
}
