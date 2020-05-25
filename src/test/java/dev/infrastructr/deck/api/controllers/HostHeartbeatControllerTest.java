package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.*;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.services.HostScriptProvider;
import dev.infrastructr.deck.data.entities.HostHeartbeat;
import dev.infrastructr.deck.data.repositories.HostHeartbeatRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class HostHeartbeatControllerTest extends WebTestBase {

    @Autowired
    private HostInitActions hostInitActions;

    @Autowired
    private HostHeartbeatRepository hostHeartbeatRepository;

    @Autowired
    private HostScriptProvider hostScriptProvider;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldGetHeartbeat() throws IOException {
        TestContext context = new TestContext();
        HostInit hostInit = hostInitActions.create(context);
        Host host = context.getHosts().get(0);
        String expectedHostHeartbeatScript = getExpectedScript(host.getId(), hostInit.getToken());

        String hostHeartbeatScript = given(documentationSpec)
            .filter(getDocument("host-heartbeat-get"))
        .when()
            .get("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), hostInit.getToken())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .extract()
            .body()
            .asString();

        assertThat(hostHeartbeatScript, is(expectedHostHeartbeatScript));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldUpdateHeartbeat() {
        TestContext context = new TestContext();
        HostInit hostInit = hostInitActions.create(context);
        Host host = context.getHosts().get(0);
        Map<String, Object> expectedHostHeartbeat = singletonMap("foo", "bar");

        given(documentationSpec)
            .filter(getDocument("host-heartbeat-create"))
            .body(expectedHostHeartbeat)
            .contentType("application/json")
        .when()
            .post("/hosts/{hostId}/heartbeat?hostToken={hostToken}", host.getId(), hostInit.getToken())
        .then()
            .assertThat()
            .statusCode(is(OK.value()));

        HostHeartbeat hostHeartbeat = hostHeartbeatRepository.findByHostId(host.getId()).orElseThrow();
        assertEquals(hostHeartbeat.getValue(), expectedHostHeartbeat);

        contextCleaner.clean(context);
    }

    private String getExpectedScript(UUID hostId, String hostAccessToken) throws IOException {
        return IOUtils.toString(
            hostScriptProvider.getHeartbeatScript(hostId, hostAccessToken).getInputStream(),
            UTF_8
        );
    }
}
