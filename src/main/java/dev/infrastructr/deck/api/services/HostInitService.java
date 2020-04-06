package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.security.authorizers.HostTokenAuthorizer;
import dev.infrastructr.deck.security.authorizers.HostAuthorizer;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HostInitService {

    private final HostTokenService hostTokenService;

    private final HostAuthorizer hostAuthorizer;

    private final InitCommandGenerator initCommandGenerator;

    private final HostTokenAuthorizer hostTokenAuthorizer;

    private final HostScriptProvider hostScriptProvider;

    public HostInitService(
        HostTokenService hostTokenService,
        HostAuthorizer hostAuthorizer,
        InitCommandGenerator initCommandGenerator,
        HostTokenAuthorizer hostTokenAuthorizer,
        HostScriptProvider hostScriptProvider
    ){
        this.hostTokenService = hostTokenService;
        this.hostAuthorizer = hostAuthorizer;
        this.initCommandGenerator = initCommandGenerator;
        this.hostTokenAuthorizer = hostTokenAuthorizer;
        this.hostScriptProvider = hostScriptProvider;
    }

    public HostInit createByHostId(UUID hostId){
        hostAuthorizer.authorizeByHostId(hostId);

        String hostToken = hostTokenService.createByHostId(hostId);

        HostInit hostInit = new HostInit();

        hostInit.setToken(hostToken);
        hostInit.setCommand(initCommandGenerator.generate(hostId, hostToken));

        return hostInit;
    }

    public Resource getInitByIdAndHostToken(UUID hostId, String hostToken) {
        hostTokenAuthorizer.authorizeByHostIdAndHostToken(hostId, hostToken);

        return hostScriptProvider.getHostInitScript(hostId, hostToken);
    }
}
