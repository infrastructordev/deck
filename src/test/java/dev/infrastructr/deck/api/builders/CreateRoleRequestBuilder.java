package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateRoleRequest;

import java.util.UUID;

public class CreateRoleRequestBuilder {

    private UUID playbookId;

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private CreateRoleRequestBuilder(){
    }

    public static CreateRoleRequestBuilder createRoleRequest(){
        return new CreateRoleRequestBuilder();
    }

    public CreateRoleRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateRoleRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateRoleRequestBuilder withPlaybookId(UUID playbookId){
        this.playbookId = playbookId;
        return this;
    }

    public CreateRoleRequest build(){
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName(name);
        createRoleRequest.setDescription(description);
        createRoleRequest.setPlaybookId(playbookId);
        return createRoleRequest;
    }
}
