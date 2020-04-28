package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Provider;

import java.util.UUID;

public class ProviderOwnerIdSpec extends IdSpec<Provider> {

    public ProviderOwnerIdSpec(UUID ownerId){
        super(ownerId, "owner");
    }
}
