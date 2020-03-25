package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.User;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.repositories.UserRepository;
import dev.infrastructr.deck.api.mappers.UserMapper;
import dev.infrastructr.deck.security.providers.ContextBackedUserDetailsProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserMapper userMapper;

    private ContextBackedUserDetailsProvider contextBackedUserDetailsProvider;

    public UserService(
        UserRepository userRepository,
        UserMapper userMapper,
        ContextBackedUserDetailsProvider contextBackedUserDetailsProvider
    ){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.contextBackedUserDetailsProvider = contextBackedUserDetailsProvider;
    }

    public User getCurrentUser(){
        UserDetails userDetails = contextBackedUserDetailsProvider.getCurrent()
            .orElseThrow(NotFoundException::new);

        return userMapper.map(
            userRepository.findByNameIgnoreCase(userDetails.getUsername())
            .orElseThrow(NotFoundException::new)
        );
    }
}
