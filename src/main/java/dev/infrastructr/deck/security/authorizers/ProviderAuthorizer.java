package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Provider;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.ProviderRepository;
import dev.infrastructr.deck.security.exceptions.ForbiddenException;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
public class ProviderAuthorizer {

    private final CurrentUserProvider currentUserProvider;

    private final ProviderRepository providerRepository;

    public ProviderAuthorizer(
        CurrentUserProvider currentUserProvider,
        ProviderRepository providerRepository
    ){
        this.currentUserProvider = currentUserProvider;
        this.providerRepository = providerRepository;
    }

    public void authorizeByProviderId(UUID projectId){
        Provider provider = providerRepository.findById(projectId).orElseThrow(NotFoundException::new);

        authorizeByProvider(provider);
    }

    public void authorizeByProvider(Provider project){
        User user = currentUserProvider.getCurrentUser();

        if(!isUserAndProviderFromSameOrganization(user, project)){
            throw new ForbiddenException(
                format("User {0} has no access to project {1}.", user.getId(), project.getId())
            );
        }
    }

    boolean isUserAndProviderFromSameOrganization(User user, Provider provider){
        return provider.getOwner().getId().equals(user.getOrganization().getId());
    }
}
