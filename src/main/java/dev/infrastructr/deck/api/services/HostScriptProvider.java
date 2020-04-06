package dev.infrastructr.deck.api.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.infrastructr.deck.api.services.InitCommandGenerator.HOST_ACCESS_TOKEN_TEMPLATE_VAR_NAME;
import static dev.infrastructr.deck.api.services.InitCommandGenerator.HOST_HEARTBEAT_URL_TEMPLATE_VAR_NAME;

@Service
public class HostScriptProvider {

    private final ResourceRenderer resourceRenderer;

    private final ResourceLoader resourceLoader;

    private final AppUrlProvider appUrlProvider;

    public HostScriptProvider(
        ResourceRenderer resourceRenderer,
        ResourceLoader resourceLoader,
        AppUrlProvider appUrlProvider) {
        this.resourceRenderer = resourceRenderer;
        this.resourceLoader = resourceLoader;
        this.appUrlProvider = appUrlProvider;
    }

    public Resource getHostInitScript(UUID hostId, String hostToken){
        return getScript("classpath:scripts/host-init.sh", hostId, hostToken);
    }

    public Resource getHeartbeatScript(UUID hostId, String hostToken){
        return getScript("classpath:scripts/host-heartbeat.sh", hostId, hostToken);
    }

    private Resource getScript(String resourcePath, UUID hostId, String hostToken){
        return resourceRenderer.render(
            resourceLoader.getResource(resourcePath),
            getContext(appUrlProvider.getHostHeartbeatUrl(hostId), hostToken)
        );
    }

    private Map<String, Object> getContext(String hostHeartbeatUrl, String hostToken){
        Map<String, Object> context = new HashMap<>();
        context.put(HOST_HEARTBEAT_URL_TEMPLATE_VAR_NAME, hostHeartbeatUrl);
        context.put(HOST_ACCESS_TOKEN_TEMPLATE_VAR_NAME, hostToken);
        return context;
    }
}
