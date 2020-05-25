package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.services.HostScriptProvider;
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
    private ContextCleaner contextCleaner;

    @Autowired
    private HostActions hostActions;

    @Autowired
    private HostInitActions hostInitActions;

    @Autowired
    private HostScriptProvider hostScriptProvider;

    @Test
    public void shouldCreateInit(){
        TestContext context = new TestContext();
        Host host = hostActions.create(context);

        HostInit hostInit = given(documentationSpec)
            .filter(getDocument("host-init-create"))
            .cookie(context.getCookie())
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

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetInit() throws IOException {
        TestContext context = new TestContext();
        Host host = hostActions.create(context);
        HostInit hostInit = hostInitActions.create(context, host.getId());
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

        contextCleaner.clean(context);
    }

    private String getExpectedScript(UUID hostId, String hostAccessToken) throws IOException {
        return IOUtils.toString(
            hostScriptProvider.getHostInitScript(hostId, hostAccessToken).getInputStream(),
            UTF_8.toString()
        );
    }
}
