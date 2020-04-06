package dev.infrastructr.deck.api.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
public class InitCommandGenerator {

    public static final String HOST_ACCESS_TOKEN_TEMPLATE_VAR_NAME = "hostToken";

    public static final String HOST_HEARTBEAT_URL_TEMPLATE_VAR_NAME = "hostHeartbeatUrl";

    private static final String COMMAND_TEMPLATE = "curl -s {0}?hostToken={1} | sh";

    private final AppUrlProvider appUrlProvider;

    public InitCommandGenerator(AppUrlProvider appUrlProvider){
        this.appUrlProvider = appUrlProvider;
    }

    public String generate(UUID hostId, String hostToken){
        return format(
            COMMAND_TEMPLATE,
            appUrlProvider.getHostInitUrl(hostId),
            hostToken
        );
    }
}
