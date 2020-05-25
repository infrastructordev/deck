package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.builders.CreateRoleRequestBuilder;
import dev.infrastructr.deck.api.entities.Playbook;
import dev.infrastructr.deck.api.entities.Role;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class RoleActions {

    @Autowired
    private PlaybookActions playbookActions;

    public Role create(TestContext context){
        Playbook playbook = playbookActions.create(context);
        return create(context, playbook.getId());
    }

    public Role create(TestContext context, UUID playbookId){
        CreateRoleRequest request = CreateRoleRequestBuilder.createRoleRequest().build();

        Role role = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/playbooks/{playbookId}/roles", playbookId)
        .then()
            .extract()
            .body()
            .as(Role.class);

        context.getRoles().add(role);

        return role;
    }
}
