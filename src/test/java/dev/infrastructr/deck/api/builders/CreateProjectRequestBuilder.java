package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateProjectRequest;

public class CreateProjectRequestBuilder {

    private String name = DataFaker.getInstance().commerce().productName();

    private CreateProjectRequestBuilder(){
    }

    public static CreateProjectRequestBuilder createProjectRequest(){
        return new CreateProjectRequestBuilder();
    }

    public CreateProjectRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateProjectRequest build(){
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setName(name);
        return createProjectRequest;
    }
}
