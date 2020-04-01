package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import io.restassured.http.Cookie;
import org.springframework.stereotype.Service;

import static dev.infrastructr.deck.api.builders.CreateProjectRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;

@Service
public class ProjectActions {

    public Project create(Cookie cookie){
        CreateProjectRequest request = createProjectRequest().build();

        return given()
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects")
        .then()
            .extract()
            .body()
            .as(Project.class);
    }
}
