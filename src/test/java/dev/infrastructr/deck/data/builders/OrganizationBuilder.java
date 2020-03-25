package dev.infrastructr.deck.data.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.data.entities.Organization;

public class OrganizationBuilder {

    private String name = DataFaker.getInstance().company().name();

    private OrganizationBuilder(){
        super();
    }

    public static OrganizationBuilder organization(){
        return new OrganizationBuilder();
    }

    public OrganizationBuilder withName(String name){
        this.name = name;
        return this;
    }

    public Organization build(){
        Organization organization = new Organization();
        organization.setName(name);
        return organization;
    }
}
