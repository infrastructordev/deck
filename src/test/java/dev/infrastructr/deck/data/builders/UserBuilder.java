package dev.infrastructr.deck.data.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.data.entities.Organization;
import dev.infrastructr.deck.data.entities.Role;
import dev.infrastructr.deck.data.entities.User;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.util.StringUtils.hasText;

public class UserBuilder {

    private boolean enabled = true;

    private String name = DataFaker.getInstance().name().username();

    private String password = DataFaker.getInstance().crypto().md5();

    private List<Role> roles = singletonList(Role.OWNER);

    private Organization organization;

    private UserBuilder(){
        super();
    }

    public static UserBuilder user(){
        return new UserBuilder();
    }

    public UserBuilder withEnabled(boolean enabled){
        this.enabled = enabled;
        return this;
    }

    public UserBuilder withName(String name){
        this.name = name;
        return this;
    }

    public UserBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public UserBuilder withRoles(Role ... roles){
        this.roles = asList(roles);
        return this;
    }

    public UserBuilder withOrganization(Organization organization){
        this.organization = organization;
        return this;
    }

    public User build(){
        User user = new User();
        user.setEnabled(enabled);
        user.setName(name);
        if(hasText(password)) {
            user.setPassword(createDelegatingPasswordEncoder().encode(password));
        }
        user.setRoles(roles);
        user.setOrganization(organization);
        return user;
    }
}
