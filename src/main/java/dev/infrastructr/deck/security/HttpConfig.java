package dev.infrastructr.deck.security;

import dev.infrastructr.deck.security.handlers.RestAuthenticationEntryPoint;
import dev.infrastructr.deck.security.handlers.RestAuthenticationFailureHandler;
import dev.infrastructr.deck.security.handlers.RestAuthenticationSuccessHandler;
import dev.infrastructr.deck.security.handlers.RestLogoutSuccessHandler;
import dev.infrastructr.deck.security.props.RememberMeProperties;
import dev.infrastructr.deck.security.props.RequestMappingProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class HttpConfig extends WebSecurityConfigurerAdapter {

    private final RememberMeProperties rememberMeProperties;

    private final RequestMappingProperties requestMappingProperties;

    private final RememberMeServices rememberMeServices;

    public HttpConfig(
        RememberMeProperties rememberMeProperties,
        RequestMappingProperties requestMappingProperties,
        RememberMeServices rememberMeServices
    ){
        this.rememberMeProperties = rememberMeProperties;
        this.requestMappingProperties = requestMappingProperties;
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/public/**/*").permitAll()
        .and()
            .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and()
            .formLogin()
            .loginProcessingUrl(requestMappingProperties.getLoginUrl())
            .successHandler(new RestAuthenticationSuccessHandler())
            .failureHandler(new RestAuthenticationFailureHandler())
        .and()
            .logout()
            .deleteCookies(rememberMeProperties.getCookie())
            .logoutUrl(requestMappingProperties.getLogoutUrl())
            .logoutSuccessHandler(new RestLogoutSuccessHandler())
        .and()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .key(rememberMeProperties.getKey())
        .and()
            .httpBasic().disable()
            .csrf().disable();
    }
}
