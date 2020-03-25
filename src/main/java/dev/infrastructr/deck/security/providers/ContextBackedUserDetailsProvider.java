package dev.infrastructr.deck.security.providers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ContextBackedUserDetailsProvider {

    public Optional<UserDetails> getCurrent() {
        Authentication authentication = getCurrentAuthentication();
        if (isNull(authentication)) {
            return Optional.empty();
        }
        UserDetails user = extractUser(authentication);
        return Optional.ofNullable(user);
    }

    private Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private UserDetails extractUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User)principal;
        } else {
            return null;
        }
    }
}
