package dev.infrastructr.deck.security.services;

import dev.infrastructr.deck.data.models.User;
import dev.infrastructr.deck.data.repositories.UserRepository;
import dev.infrastructr.deck.security.mappers.UserAuthDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class AuthDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    private UserAuthDetailsMapper userAuthDetailsMapper;

    public AuthDetailsService(
        UserRepository userRepository,
       UserAuthDetailsMapper userAuthDetailsMapper
    ){
        this.userRepository = userRepository;
        this.userAuthDetailsMapper = userAuthDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByNameIgnoreCase(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(format("User with name {0} not found.", name));
        }
        return userAuthDetailsMapper.map(user.get());
    }
}

