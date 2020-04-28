package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateInventoryRequest;

import java.util.UUID;

public class CreateInventoryRequestBuilder {

    private UUID projectId;

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private CreateInventoryRequestBuilder(){
    }

    public static CreateInventoryRequestBuilder createInventoryRequest(){
        return new CreateInventoryRequestBuilder();
    }

    public CreateInventoryRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateInventoryRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateInventoryRequestBuilder withProjectId(UUID project){
        this.projectId = project;
        return this;
    }

    public CreateInventoryRequest build(){
        CreateInventoryRequest createInventoryRequest = new CreateInventoryRequest();
        createInventoryRequest.setName(name);
        createInventoryRequest.setDescription(description);
        createInventoryRequest.setProjectId(projectId);
        return createInventoryRequest;
    }
}
