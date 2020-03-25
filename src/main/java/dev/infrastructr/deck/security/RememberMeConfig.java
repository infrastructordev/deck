package dev.infrastructr.deck.security;

import dev.infrastructr.deck.security.props.RememberMeProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Configuration
public class RememberMeConfig {

    @Bean
    public RememberMeServices tokenBasedRememberMeServices(
        UserDetailsService userDetailsService,
        RememberMeProperties rememberMeProperties
    ) {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
            rememberMeProperties.getKey(), userDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(rememberMeProperties.getExpiry());
        rememberMeServices.setUseSecureCookie(rememberMeProperties.isHttpsOnly());
        rememberMeServices.setCookieName(rememberMeProperties.getCookie());
        return rememberMeServices;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return createDelegatingPasswordEncoder();
    }
}
