package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateHostRequest;

import java.util.UUID;

public class CreateHostRequestBuilder {

    private UUID projectId;

    private String name = DataFaker.getInstance().commerce().productName();

    private CreateHostRequestBuilder(){
    }

    public static CreateHostRequestBuilder createProjectRequest(){
        return new CreateHostRequestBuilder();
    }

    public CreateHostRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateHostRequestBuilder withProjectId(UUID project){
        this.projectId = project;
        return this;
    }

    public CreateHostRequest build(){
        CreateHostRequest createHostRequest = new CreateHostRequest();
        createHostRequest.setName(name);
        createHostRequest.setProjectId(projectId);
        return createHostRequest;
    }
}
