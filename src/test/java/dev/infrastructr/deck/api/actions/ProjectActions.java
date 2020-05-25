package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.infrastructr.deck.api.builders.CreateProjectRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;

@Service
public class ProjectActions {

    @Autowired
    private ProviderActions providerActions;

    public Project create(TestContext context){
        Provider provider = providerActions.create(context);
        return create(context, provider.getId());
    }

    public Project create(TestContext context, UUID providerId){
        CreateProjectRequest request = createProjectRequest().withProviderId(providerId).build();

        Project project = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects")
        .then()
            .extract()
            .body()
            .as(Project.class);

        context.getProjects().add(project);

        return project;
    }
}
