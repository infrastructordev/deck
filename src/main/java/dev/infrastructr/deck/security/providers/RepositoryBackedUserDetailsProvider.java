package dev.infrastructr.deck.security.providers;

import dev.infrastructr.deck.data.entities.User;
import dev.infrastructr.deck.data.repositories.UserRepository;
import dev.infrastructr.deck.security.mappers.UserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class RepositoryBackedUserDetailsProvider implements UserDetailsService {

    private UserRepository userRepository;

    private UserDetailsMapper userDetailsMapper;

    public RepositoryBackedUserDetailsProvider(
        UserRepository userRepository,
        UserDetailsMapper userDetailsMapper
    ){
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByNameIgnoreCase(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(format("User with name {0} not found.", name));
        }
        return userDetailsMapper.map(user.get());
    }
}

