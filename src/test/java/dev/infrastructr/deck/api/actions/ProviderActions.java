package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import static dev.infrastructr.deck.api.builders.CreateProviderRequestBuilder.createProviderRequest;
import static io.restassured.RestAssured.given;

@Service
public class ProviderActions {

    public Provider create(Cookie cookie){
        CreateProviderRequest request = createProviderRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/providers")
        .then()
            .extract()
            .body()
            .as(Provider.class);
    }
}
