package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateGroupRequest;

import java.util.UUID;

public class CreateGroupRequestBuilder {

    private UUID inventoryId;

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private CreateGroupRequestBuilder(){
    }

    public static CreateGroupRequestBuilder createGroupRequest(){
        return new CreateGroupRequestBuilder();
    }

    public CreateGroupRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateGroupRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateGroupRequestBuilder withInventoryId(UUID inventoryId){
        this.inventoryId = inventoryId;
        return this;
    }

    public CreateGroupRequest build(){
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setName(name);
        createGroupRequest.setDescription(description);
        createGroupRequest.setInventoryId(inventoryId);
        return createGroupRequest;
    }
}
