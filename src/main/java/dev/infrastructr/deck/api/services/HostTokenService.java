package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Host;
import dev.infrastructr.deck.data.entities.HostToken;
import dev.infrastructr.deck.data.repositories.HostTokenRepository;
import dev.infrastructr.deck.data.repositories.HostRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class HostTokenService {

    private final PasswordEncoder passwordEncoder;

    private final HostRepository hostRepository;

    private final HostTokenRepository hostTokenRepository;

    public HostTokenService(
        PasswordEncoder passwordEncoder,
        HostRepository hostRepository,
        HostTokenRepository hostTokenRepository
    ){
        this.passwordEncoder = passwordEncoder;
        this.hostRepository = hostRepository;
        this.hostTokenRepository = hostTokenRepository;
    }

    public String createByHostId(UUID hostId) {
        Host host = hostRepository.findById(hostId).orElseThrow(NotFoundException::new);
        Optional<HostToken> hostTokenOptional = hostTokenRepository.findByHostId(hostId);

        String rawHostToken = generate();
        String encodedToken = encode(rawHostToken);

        if (hostTokenOptional.isPresent()) {
            HostToken hostToken = hostTokenOptional.get();
            hostToken.setValue(encodedToken);
        } else {
            HostToken hostToken = new HostToken();

            hostToken.setValue(encodedToken);
            hostToken.setHost(host);

            hostTokenRepository.save(hostToken);
        }

        return rawHostToken;
    }

    public boolean matchesByHostIdAndHostToken(UUID hostId, String rawHostToken){
        HostToken hostToken = hostTokenRepository.findByHostId(hostId).orElseThrow(NotFoundException::new);
        return passwordEncoder.matches(rawHostToken, hostToken.getValue());
    }

    private String generate(){
        return UUID.randomUUID().toString();
    }

    private String encode(String token){
        return passwordEncoder.encode(token);
    }
}
