package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreatePlaybookRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;
import dev.infrastructr.deck.data.repositories.PlaybookRepository;
import io.restassured.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class PlaybookActions {

    @Autowired
    private ProjectActions projectActions;

    public Playbook create(TestContext context){
        Project project = projectActions.create(context);
        return create(context, project.getId());
    }

    public Playbook create(TestContext context, UUID projectId){
        CreatePlaybookRequest request = CreatePlaybookRequestBuilder.createPlaybookRequest().build();

        Playbook playbook = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/playbooks", projectId)
        .then()
            .extract()
            .body()
            .as(Playbook.class);

        context.getPlaybooks().add(playbook);

        return playbook;
    }
}
