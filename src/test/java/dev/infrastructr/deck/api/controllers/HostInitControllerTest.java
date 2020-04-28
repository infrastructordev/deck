package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.api.entities.Inventory;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.services.HostScriptProvider;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class HostInitControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private HostInitActions hostInitActions;

    @Autowired
    private InventoryActions inventoryActions;

    @Autowired
    private HostScriptProvider hostScriptProvider;

    @Test
    public void shouldCreateInit(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Host host = hostActions.create(cookie, inventory.getId());

        HostInit hostInit = given(documentationSpec)
            .filter(getDocument("host-init-create"))
            .cookie(userActions.authenticate(user))
        .when()
            .post("/hosts/{hostId}/init", host.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .extract()
            .body()
            .as(HostInit.class);

        assertThat(hostInit.getCommand(), startsWith("curl -s"));
        assertThat(hostInit.getToken(), notNullValue());
    }

    @Test
    public void shouldGetInit() throws IOException {
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Inventory inventory = inventoryActions.create(cookie, project.getId());
        Host host = hostActions.create(cookie, inventory.getId());
        HostInit hostInit = hostInitActions.create(cookie, host.getId());
        String expectedHostInitScript = getExpectedScript(host.getId(), hostInit.getToken());

        String hostInitScript = given(documentationSpec)
            .filter(getDocument("host-init-get"))
        .when()
            .get("/hosts/{hostId}/init?hostToken={hostToken}", host.getId(), hostInit.getToken())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .extract()
            .body()
            .asString();

        assertThat(hostInitScript, is(expectedHostInitScript));
    }

    private String getExpectedScript(UUID hostId, String hostAccessToken) throws IOException {
        return IOUtils.toString(
            hostScriptProvider.getHostInitScript(hostId, hostAccessToken).getInputStream(),
            UTF_8.toString()
        );
    }
}
