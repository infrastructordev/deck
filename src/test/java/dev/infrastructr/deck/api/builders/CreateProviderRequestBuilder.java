package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.data.entities.ProviderType;

public class CreateProviderRequestBuilder {

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private String type = ProviderType.GITHUB.name();

    private CreateProviderRequestBuilder(){
    }

    public static CreateProviderRequestBuilder createProviderRequest(){
        return new CreateProviderRequestBuilder();
    }

    public CreateProviderRequestBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CreateProviderRequestBuilder withDescription(String description){
        this.description = description;
        return this;
    }

    public CreateProviderRequestBuilder withType(ProviderType type){
        this.type = type.name();
        return this;
    }

    public CreateProviderRequest build(){
        CreateProviderRequest createProviderRequest = new CreateProviderRequest();
        createProviderRequest.setName(name);
        createProviderRequest.setDescription(description);
        createProviderRequest.setType(type);
        return createProviderRequest;
    }
}
