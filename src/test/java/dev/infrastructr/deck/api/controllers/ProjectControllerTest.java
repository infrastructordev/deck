package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.ContextCleaner;
import dev.infrastructr.deck.WebTestBase;
import dev.infrastructr.deck.api.actions.ProjectActions;
import dev.infrastructr.deck.api.actions.ProviderActions;
import dev.infrastructr.deck.api.entities.Project;
import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.infrastructr.deck.api.builders.CreateProjectRequestBuilder.createProjectRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class ProjectControllerTest extends WebTestBase {

    @Autowired
    private ProviderActions providerActions;

    @Autowired
    private ProjectActions projectActions;

    @Autowired
    private ContextCleaner contextCleaner;

    @Test
    public void shouldCreate(){
        TestContext context = new TestContext();
        Provider provider = providerActions.create(context);
        CreateProjectRequest request = createProjectRequest()
            .withProviderId(provider.getId())
            .build();

        Project project = given(documentationSpec)
            .filter(getDocument("project-create"))
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/projects")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .extract().body().as(Project.class);
        context.getProjects().add(project);

        assertThat(project.getId(), is(notNullValue()));
        assertThat(project.getName(), is(request.getName()));
        assertThat(project.getDescription(), is(request.getDescription()));
        assertThat(project.getAuthor().getId(), is(context.getUser().getId()));
        assertThat(project.getAuthor().getName(), is(context.getUser().getName()));
        assertThat(project.getOwner().getId(), is(context.getOrganization().getId()));
        assertThat(project.getOwner().getName(), is(context.getOrganization().getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetAll(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);

        given(documentationSpec)
            .filter(getDocument("project-get-all"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects")
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(project.getId().toString()))
            .body("content[0].name", is(project.getName()))
            .body("content[0].description", is(project.getDescription()))
            .body("content[0].author.id", is(context.getUser().getId().toString()))
            .body("content[0].author.name", is(context.getUser().getName()))
            .body("content[0].owner.id", is(context.getOrganization().getId().toString()))
            .body("content[0].owner.name", is(context.getOrganization().getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetFiltered(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        projectActions.create(context);

        given(documentationSpec)
            .filter(getDocument("project-get-filtered"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects?filter={filter}", project.getName())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(project.getId().toString()))
            .body("content[0].name", is(project.getName()))
            .body("content[0].description", is(project.getDescription()))
            .body("content[0].author.id", is(context.getUser().getId().toString()))
            .body("content[0].author.name", is(context.getUser().getName()))
            .body("content[0].owner.id", is(context.getOrganization().getId().toString()))
            .body("content[0].owner.name", is(context.getOrganization().getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetOnePerPageSorted(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);
        Project anotherProject = projectActions.create(context);
        String sort = "name,desc";
        Project expectedProject = project.getName().compareTo(anotherProject.getName()) > 0
            ? project
            : anotherProject;

        given(documentationSpec)
            .filter(getDocument("project-get-all"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects?page=0&size=1&sort={sort}", sort)
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("content[0].id", is(expectedProject.getId().toString()))
            .body("content[0].name", is(expectedProject.getName()))
            .body("content[0].description", is(expectedProject.getDescription()))
            .body("content[0].author.id", is(context.getUser().getId().toString()))
            .body("content[0].author.name", is(context.getUser().getName()))
            .body("content[0].owner.id", is(context.getOrganization().getId().toString()))
            .body("content[0].owner.name", is(context.getOrganization().getName()));

        contextCleaner.clean(context);
    }

    @Test
    public void shouldGetById(){
        TestContext context = new TestContext();
        Project project = projectActions.create(context);

        given(documentationSpec)
            .filter(getDocument("project-get-by-id"))
            .cookie(context.getCookie())
            .contentType("application/json")
        .when()
            .get("/projects/{projectId}", project.getId())
        .then()
            .assertThat()
            .statusCode(is(OK.value()))
            .and()
            .body("id", is(project.getId().toString()))
            .body("name", is(project.getName()))
            .body("description", is(project.getDescription()))
            .body("author.id", is(context.getUser().getId().toString()))
            .body("author.name", is(context.getUser().getName()))
            .body("owner.id", is(context.getOrganization().getId().toString()))
            .body("owner.name", is(context.getOrganization().getName()));

        contextCleaner.clean(context);
    }
}
