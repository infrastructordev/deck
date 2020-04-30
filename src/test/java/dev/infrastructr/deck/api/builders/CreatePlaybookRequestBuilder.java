package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreatePlaybookRequest;

import java.util.UUID;

public class CreatePlaybookRequestBuilder {

    private UUID projectId;

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private CreatePlaybookRequestBuilder(){
    }

    public static CreatePlaybookRequestBuilder createPlaybookRequest(){
        return new CreatePlaybookRequestBuilder();
    }

    public CreatePlaybookRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreatePlaybookRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreatePlaybookRequestBuilder withProjectId(UUID project){
        this.projectId = project;
        return this;
    }

    public CreatePlaybookRequest build(){
        CreatePlaybookRequest createPlaybookRequest = new CreatePlaybookRequest();
        createPlaybookRequest.setName(name);
        createPlaybookRequest.setDescription(description);
        createPlaybookRequest.setProjectId(projectId);
        return createPlaybookRequest;
    }
}
