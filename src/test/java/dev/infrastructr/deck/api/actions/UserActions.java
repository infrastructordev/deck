package dev.infrastructr.deck.api.actions;

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
    protected String cookie;

    @Autowired
    protected TransactionalEntityManager entityManager;

    public User create(){
        return create(PASSWORD);
    }

    public User create(String password){
        return create(password, true);
    }

    public User create(String password, boolean enabled){
        Organization organization = entityManager.persist(organization().build());
        return entityManager.persist(UserBuilder.user()
            .withOrganization(organization)
            .withEnabled(enabled)
            .withPassword(password)
            .build());
    }

    public Cookie authenticate(User user){
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

    public String getCookie() {
        return cookie;
    }
}
