package dev.infrastructr.deck.git.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dev.infrastructr.deck.git")
public class GitProps {

    private String keyName;

    private String projectNamePrefix;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getProjectNamePrefix() {
        return projectNamePrefix;
    }

    public void setProjectNamePrefix(String projectNamePrefix) {
        this.projectNamePrefix = projectNamePrefix;
    }
}
