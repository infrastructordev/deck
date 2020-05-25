package dev.infrastructr.deck.api.actions;

import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.data.TransactionalEntityManager;
import dev.infrastructr.deck.data.builders.UserBuilder;
import dev.infrastructr.deck.data.entities.Organization;
import dev.infrastructr.deck.data.entities.User;
import io.restassured.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static dev.infrastructr.deck.data.builders.OrganizationBuilder.organization;
import static io.restassured.RestAssured.given;

@Service
public class UserActions {

    public static final boolean ACCOUNT_DISABLED = false;

    public static final String PASSWORD = "123456";

    @Value("${dev.infrastructr.deck.security.remember-me.cookie}")
    protected String cookieName;

    @Autowired
    protected TransactionalEntityManager entityManager;

    public User create(TestContext context){
        return create(context, PASSWORD);
    }

    public User create(TestContext context, String password){
        return create(context, password, true);
    }

    public User create(TestContext context, String password, boolean enabled){
        Organization organization = entityManager.persist(organization().build());
        User user = entityManager.persist(UserBuilder.user()
            .withOrganization(organization)
            .withEnabled(enabled)
            .withPassword(password)
            .build());

        context.setOrganization(organization);
        context.setUser(user);

        authenticate(context);

        return user;
    }

    private void authenticate(TestContext context){
        User user = context.getUser();

        Cookie cookie = given()
            .auth().none()
            .formParam("username", user.getName())
            .formParam("password", PASSWORD)
            .when()
            .post("/users/login")
            .then()
            .extract()
            .detailedCookie(cookieName);

        context.setCookie(cookie);
    }

    public String getCookieName() {
        return cookieName;
    }
}
