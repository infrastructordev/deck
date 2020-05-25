package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.ProviderMapper;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.data.entities.ProviderType;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProviderRepository;
import dev.infrastructr.deck.data.specs.ProviderFilterSpec;
import dev.infrastructr.deck.data.specs.ProviderOwnerIdSpec;
import dev.infrastructr.deck.security.authorizers.ProviderAuthorizer;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProviderService {

    private final CurrentUserProvider currentUserProvider;

    private final ProviderRepository providerRepository;

    private final ProviderMapper providerMapper;

    private final ProviderAuthorizer providerAuthorizer;

    public ProviderService(
        CurrentUserProvider currentUserProvider,
        ProviderRepository providerRepository,
        ProviderMapper providerMapper,
        ProviderAuthorizer providerAuthorizer
    ){
        this.currentUserProvider = currentUserProvider;
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.providerAuthorizer = providerAuthorizer;
    }

    public Provider create(CreateProviderRequest createProviderRequest){
        User user = currentUserProvider.getCurrentUser();

        dev.infrastructr.deck.data.entities.Provider provider = new dev.infrastructr.deck.data.entities.Provider();
        provider.setOwner(user.getOrganization());
        provider.setName(createProviderRequest.getName());
        provider.setDescription(createProviderRequest.getDescription());
        provider.setType(ProviderType.valueOf(createProviderRequest.getType()));
        provider.setToken(createProviderRequest.getToken());
        provider.setNamespace(createProviderRequest.getNamespace());

        return providerMapper.map(providerRepository.save(provider));
    }

    public Page<Provider> getAll(Pageable pageable, String filter){
        User user = currentUserProvider.getCurrentUser();
        return providerRepository
            .findAll(
                Specification
                    .where(new ProviderOwnerIdSpec(user.getOrganization().getId()))
                    .and(new ProviderFilterSpec(filter)),
                pageable
            )
            .map(providerMapper::map);
    }

    public Provider getById(UUID id){
        providerAuthorizer.authorizeByProviderId(id);

        dev.infrastructr.deck.data.entities.Provider provider = providerRepository.findById(id).orElseThrow(NotFoundException::new);

        return providerMapper.map(provider);
    }
}
