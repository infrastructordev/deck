package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;

import java.util.UUID;

public class CreateProjectRequestBuilder {

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private UUID providerId;

    private CreateProjectRequestBuilder(){
    }

    public static CreateProjectRequestBuilder createProjectRequest(){
        return new CreateProjectRequestBuilder();
    }

    public CreateProjectRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateProjectRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateProjectRequestBuilder withProviderId(UUID providerId){
        this.providerId = providerId;
        return this;
    }

    public CreateProjectRequest build(){
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setName(name);
        createProjectRequest.setDescription(description);
        createProjectRequest.setProviderId(providerId);
        return createProjectRequest;
    }
}
