package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateHostRequest;

import java.util.UUID;

public class CreateHostRequestBuilder {

    private UUID inventoryId;

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private CreateHostRequestBuilder(){
    }

    public static CreateHostRequestBuilder createHostRequest(){
        return new CreateHostRequestBuilder();
    }

    public CreateHostRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateHostRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateHostRequestBuilder withInventoryId(UUID inventoryId){
        this.inventoryId = inventoryId;
        return this;
    }

    public CreateHostRequest build(){
        CreateHostRequest createHostRequest = new CreateHostRequest();
        createHostRequest.setName(name);
        createHostRequest.setDescription(description);
        createHostRequest.setInventoryId(inventoryId);
        return createHostRequest;
    }
}
