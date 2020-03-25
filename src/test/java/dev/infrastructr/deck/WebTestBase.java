package dev.infrastructr.deck;

import dev.infrastructr.deck.data.TransactionalEntityManager;
import dev.infrastructr.deck.data.builders.UserBuilder;
import dev.infrastructr.deck.data.entities.Organization;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.Cookie;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import static dev.infrastructr.deck.data.builders.OrganizationBuilder.organization;
import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestDocs
@AutoConfigureTestEntityManager
public class WebTestBase {

    protected static final boolean ACCOUNT_DISABLED = false;

    protected static final String PASSWORD = "123456";

    @Value("${dev.infrastructr.deck.security.remember-me.cookie}")
    protected String cookie;

    @LocalServerPort
    private int port;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    protected TransactionalEntityManager entityManager;

    protected RequestSpecification documentationSpec;

    @Before
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(ALL);
        this.documentationSpec = new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation)).build();
    }

    protected Filter getDocument(String documentId){
        return document(documentId,
            preprocessRequest(modifyUris()
                 .scheme("https")
                .host("api.infrastructr.dev")
                .removePort()));
    }

    protected User user(String password){
        return user(password, true);
    }

    protected User user(String password, boolean enabled){
        Organization organization = entityManager.persist(organization().build());
        return entityManager.persist(UserBuilder.user()
            .withOrganization(organization)
            .withEnabled(enabled)
            .withPassword(password)
            .build());
    }

    protected Cookie authenticate(User user){
        return given()
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", PASSWORD)
            .when()
            .post("/users/login")
            .then()
            .extract()
            .detailedCookie(cookie);
    }
}
