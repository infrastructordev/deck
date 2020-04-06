package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Host;
import dev.infrastructr.deck.data.entities.HostHeartbeat;
import dev.infrastructr.deck.data.repositories.HostHeartbeatRepository;
import dev.infrastructr.deck.data.repositories.HostRepository;
import dev.infrastructr.deck.security.authorizers.HostTokenAuthorizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class HostHeartbeatService {

    private final HostHeartbeatRepository hostHeartbeatRepository;

    private final HostRepository hostRepository;

    private final HostTokenAuthorizer hostTokenAuthorizer;

    private final HostScriptProvider hostScriptProvider;

    public HostHeartbeatService(
        HostHeartbeatRepository hostHeartbeatRepository,
        HostRepository hostRepository,
        HostTokenAuthorizer hostTokenAuthorizer,
        HostScriptProvider hostScriptProvider
    ){
        this.hostHeartbeatRepository = hostHeartbeatRepository;
        this.hostRepository = hostRepository;
        this.hostTokenAuthorizer = hostTokenAuthorizer;
        this.hostScriptProvider = hostScriptProvider;
    }

    public Resource getByHostIdAndHostToken(UUID hostId, String hostToken) {
        hostTokenAuthorizer.authorizeByHostIdAndHostToken(hostId, hostToken);

        return hostScriptProvider.getHeartbeatScript(hostId, hostToken);
    }

    public void updateByHostIdAndHostToken(
        UUID hostId,
        String hostToken,
        Map<String, Object> heartBeatRequest
    ){
        hostTokenAuthorizer.authorizeByHostIdAndHostToken(hostId, hostToken);

        Optional<HostHeartbeat> hostHeartbeatOptional = hostHeartbeatRepository.findByHostId(hostId);

        if(hostHeartbeatOptional.isEmpty()){
            Host host = hostRepository.findById(hostId).orElseThrow(NotFoundException::new);
            HostHeartbeat hostHeartbeat = new HostHeartbeat();
            hostHeartbeat.setHost(host);
            hostHeartbeat.setValue(heartBeatRequest);
            hostHeartbeatRepository.save(hostHeartbeat);
        } else {
            HostHeartbeat hostHeartbeat = hostHeartbeatOptional.get();
            hostHeartbeat.setValue(heartBeatRequest);
        }
    }
}
