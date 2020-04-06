package dev.infrastructr.deck;

import dev.infrastructr.deck.api.props.ApiCommonProps;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

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
public abstract class WebTestBase {

    @LocalServerPort
    private int port;

    @Autowired
    protected ApiCommonProps apiCommonProps;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    protected RequestSpecification documentationSpec;

    @Before
    public void init() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(ALL);
        this.documentationSpec = new RequestSpecBuilder()
            .addFilter(documentationConfiguration(restDocumentation)).build();
        this.apiCommonProps.setBaseUrl("http://api.infrastructr.local:" + port);
    }

    protected Filter getDocument(String documentId){
        return document(documentId,
            preprocessRequest(modifyUris()
                 .scheme("https")
                .host("api.infrastructr.dev")
                .removePort()));
    }
}
