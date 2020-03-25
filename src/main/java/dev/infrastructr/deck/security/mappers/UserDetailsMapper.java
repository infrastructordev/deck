package dev.infrastructr.deck.security.mappers;

import dev.infrastructr.deck.data.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class UserDetailsMapper {

    public UserDetails map(dev.infrastructr.deck.data.entities.User user){
        return User.withUsername(user.getName())
            .password(user.getPassword())
            .disabled(!user.isEnabled())
            .authorities(map(user.getRoles()))
            .build();
    }

    Collection<? extends GrantedAuthority> map(List<Role> roles){
        if (isEmpty(roles)) {
            return emptySet();
        }

        return roles.stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
