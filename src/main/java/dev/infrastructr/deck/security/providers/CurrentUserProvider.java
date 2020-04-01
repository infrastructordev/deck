package dev.infrastructr.deck.security.providers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProvider {

    private final UserRepository userRepository;

    private final ContextBackedUserDetailsProvider contextBackedUserDetailsProvider;

    public CurrentUserProvider(
        UserRepository userRepository,
        ContextBackedUserDetailsProvider contextBackedUserDetailsProvider
    ){
        this.userRepository = userRepository;
        this.contextBackedUserDetailsProvider = contextBackedUserDetailsProvider;
    }

    public User getCurrentUser(){
        UserDetails userDetails = contextBackedUserDetailsProvider.getCurrent()
            .orElseThrow(NotFoundException::new);

        return userRepository.findByNameIgnoreCase(userDetails.getUsername())
            .orElseThrow(NotFoundException::new);
    }
}
