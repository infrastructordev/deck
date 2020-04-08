package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.HostActions;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.UserActions;
import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateHostRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

public class HostControllerTest extends WebTestBase {

    @Autowired
    private UserActions userActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private HostActions hostActions;

    @Test
    public void shouldCreate(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        CreateHostRequest request = createProjectRequest()
            .withProjectId(project.getId())
            .build();

        given(documentationSpec)
            .filter(getDocument("host-create"))
            .cookie(cookie)
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects/{projectId}/hosts", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(notNullValue()))
            .body("name", is(request.getName()));
    }

    @Test
    public void shouldGetByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("host-get-by-project-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/hosts", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(host.getId().toString()))
            .body("content[0].name", is(host.getName()));
    }

    @Test
    public void shouldGetOnePerPageSortedByProjectId(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());
        Host anotherHost = hostActions.create(cookie, project.getId());
        String sort = "name,desc";
        Host expectedHost = host.getName().compareTo(anotherHost.getName()) > 0
            ? host
            : anotherHost;

        given(documentationSpec)
            .filter(getDocument("host-get-by-project-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}/hosts?page=0&size=1&sort={sort}", project.getId(), sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedHost.getId().toString()))
            .body("content[0].name", is(expectedHost.getName()));
    }

    @Test
    public void shouldGetById(){
        User user = userActions.create();
        Cookie cookie = userActions.authenticate(user);
        Project project = projectActions.create(cookie);
        Host host = hostActions.create(cookie, project.getId());

        given(documentationSpec)
            .filter(getDocument("host-get-by-id"))
            .cookie(userActions.authenticate(user))
            .contentType("application/json")
        .when()
            .get("/hosts/{hostId}", host.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(host.getId().toString()))
            .body("name", is(host.getName()));
    }
}
