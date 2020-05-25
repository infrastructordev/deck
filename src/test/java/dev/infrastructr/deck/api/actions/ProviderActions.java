package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.data.entities.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static dev.infrastructr.deck.api.builders.CreateProviderRequestBuilder.createProviderRequest;
import static io.restassured.RestAssured.given;

@Service
public class ProviderActions {

    @Autowired
    private UserActions userActions;

    @Value("${dev.infrastructr.deck.test.provider.token}")
    protected String token;

    @Value("${dev.infrastructr.deck.test.provider.namespace}")
    private String namespace;

    public Provider create(TestContext context){
        if(context.getCookie() == null){
            userActions.create(context);
        }
        CreateProviderRequest request = createProviderRequest()
           .withType(ProviderType.GITHUB)
           .withToken(token)
           .withNamespace(namespace)
           .build();

        Provider provider = given()
            .cookie(context.getCookie())
            .body(request)
            .contentType("application/json")
        .when()
            .post("/providers")
        .then()
            .extract()
            .body()
            .as(Provider.class);

        context.getProviders().add(provider);

        return provider;
    }
}
