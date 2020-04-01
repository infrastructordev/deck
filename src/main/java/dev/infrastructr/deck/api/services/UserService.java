package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.User;
import dev.infrastructr.deck.api.mappers.UserMapper;
import dev.infrastructr.deck.security.providers.CurrentUserProvider;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final CurrentUserProvider currentUserProvider;

    public UserService(
        UserMapper userMapper,
        CurrentUserProvider currentUserProvider
    ){
        this.userMapper = userMapper;
        this.currentUserProvider = currentUserProvider;
    }

    public User getCurrentUser(){
        return userMapper.map(currentUserProvider.getCurrentUser());
    }
}
