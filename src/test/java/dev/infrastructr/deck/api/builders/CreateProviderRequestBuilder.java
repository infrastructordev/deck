package dev.infrastructr.deck.api.builders;

import dev.infrastructr.deck.DataFaker;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.data.entities.ProviderType;

public class CreateProviderRequestBuilder {

    private String name = DataFaker.getInstance().commerce().productName();

    private String description = DataFaker.getInstance().commerce().material();

    private String type = ProviderType.GITHUB.name();

    private String token;

    private String namespace;

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

    public CreateProviderRequestBuilder withToken(String token){
        this.token = token;
        return this;
    }

    public CreateProviderRequestBuilder withNamespace(String namespace){
        this.namespace = namespace;
        return this;
    }

    public CreateProviderRequest build(){
        CreateProviderRequest createProviderRequest = new CreateProviderRequest();
        createProviderRequest.setName(name);
        createProviderRequest.setDescription(description);
        createProviderRequest.setType(type);
        createProviderRequest.setToken(token);
        createProviderRequest.setNamespace(namespace);
        return createProviderRequest;
    }
}
