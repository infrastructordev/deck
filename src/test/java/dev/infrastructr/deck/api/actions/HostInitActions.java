package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.api.models.TestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@Service
public class HostInitActions {

    @Autowired
    private HostActions hostActions;

    public HostInit create(TestContext context){
        Host host = hostActions.create(context);
        return create(context, host.getId());
    }

    public HostInit create(TestContext context, UUID hostId) {
        HostInit hostInit = given()
            .cookie(context.getCookie())
        .when()
            .post("/hosts/{hostId}/init", hostId)
        .then()
            .extract()
            .body()
            .as(HostInit.class);

        context.getHostInits().add(hostInit);

        return hostInit;
    }
}
