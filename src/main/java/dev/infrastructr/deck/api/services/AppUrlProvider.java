package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.props.ApiCommonProps;
import dev.infrastructr.deck.api.props.ApiRequestMappingProps;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.text.MessageFormat.format;

@Service
public class AppUrlProvider {

    private final ApiRequestMappingProps apiHostProps;

    private final ApiCommonProps apiCommonProps;

    public AppUrlProvider(
        ApiRequestMappingProps apiHostProps,
        ApiCommonProps apiCommonProps
    ){
        this.apiHostProps = apiHostProps;
        this.apiCommonProps = apiCommonProps;
    }

    public String getHostHeartbeatUrl(UUID hostId){
        return format(apiCommonProps.getBaseUrl() + apiHostProps.getHostHeartbeat(), hostId);
    }

    public String getHostInitUrl(UUID hostId){
        return format(apiCommonProps.getBaseUrl() + apiHostProps.getHostInit(), hostId);
    }
}
