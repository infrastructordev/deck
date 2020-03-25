package dev.infrastructr.deck.security.mappers;

import dev.infrastructr.deck.data.models.Role;
import dev.infrastructr.deck.data.models.User;
import dev.infrastructr.deck.security.models.AuthDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Mapper
public abstract class UserAuthDetailsMapper {

    @Mapping(target = "username", source = "name")
    @Mapping(target = "authorities", source = "roles", qualifiedByName = "mapRolesToAuthorities")
    public abstract AuthDetails map(User user);

    @Named("mapRolesToAuthorities")
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
