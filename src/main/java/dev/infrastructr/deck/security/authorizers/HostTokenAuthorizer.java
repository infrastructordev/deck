package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.services.HostTokenService;
import dev.infrastructr.deck.security.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
public class HostTokenAuthorizer {

    private final HostTokenService hostTokenService;

    public HostTokenAuthorizer(HostTokenService hostTokenService){
        this.hostTokenService = hostTokenService;
    }

    public void authorizeByHostIdAndHostToken(UUID hostId, String hostToken){
        if(!hostTokenService.matchesByHostIdAndHostToken(hostId, hostToken)){
            throw new ForbiddenException(format("Invalid host token provided for host {0}.", hostId));
        }
    }
}
