package dev.infrastructr.deck.security;

import dev.infrastructr.deck.security.props.SecurityRememberMeProps;
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
        SecurityRememberMeProps securityRememberMeProps
    ) {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(
            securityRememberMeProps.getKey(), userDetailsService);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setTokenValiditySeconds(securityRememberMeProps.getExpiry());
        rememberMeServices.setUseSecureCookie(securityRememberMeProps.isHttpsOnly());
        rememberMeServices.setCookieName(securityRememberMeProps.getCookie());
        return rememberMeServices;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return createDelegatingPasswordEncoder();
    }
}
