package dev.infrastructr.deck.api.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dev.infrastructr.deck.api.request-mapping")
public class ApiRequestMappingProps {

    private String hostInit;

    private String hostHeartbeat;

    public String getHostInit() {
        return hostInit;
    }

    public void setHostInit(String hostInit) {
        this.hostInit = hostInit;
    }

    public String getHostHeartbeat() {
        return hostHeartbeat;
    }

    public void setHostHeartbeat(String hostHeartbeat) {
        this.hostHeartbeat = hostHeartbeat;
    }
}
