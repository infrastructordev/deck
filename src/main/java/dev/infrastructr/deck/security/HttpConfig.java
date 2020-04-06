package dev.infrastructr.deck.security;

import dev.infrastructr.deck.security.handlers.RestAuthenticationEntryPoint;
import dev.infrastructr.deck.security.handlers.RestAuthenticationFailureHandler;
import dev.infrastructr.deck.security.handlers.RestAuthenticationSuccessHandler;
import dev.infrastructr.deck.security.handlers.RestLogoutSuccessHandler;
import dev.infrastructr.deck.security.props.SecurityRememberMeProps;
import dev.infrastructr.deck.security.props.SecurityRequestMappingProps;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class HttpConfig extends WebSecurityConfigurerAdapter {

    private final SecurityRememberMeProps securityRememberMeProps;

    private final SecurityRequestMappingProps securityRequestMappingProps;

    private final RememberMeServices rememberMeServices;

    public HttpConfig(
        SecurityRememberMeProps securityRememberMeProps,
        SecurityRequestMappingProps securityRequestMappingProps,
        RememberMeServices rememberMeServices
    ){
        this.securityRememberMeProps = securityRememberMeProps;
        this.securityRequestMappingProps = securityRequestMappingProps;
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/hosts/**/init")
            .permitAll()
        .and()
            .authorizeRequests()
            .antMatchers("/hosts/**/heartbeat")
            .permitAll()
        .and()
            .authorizeRequests()
            .anyRequest().authenticated()
        .and()
            .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and()
            .formLogin()
            .loginProcessingUrl(securityRequestMappingProps.getLogin())
            .successHandler(new RestAuthenticationSuccessHandler())
            .failureHandler(new RestAuthenticationFailureHandler())
        .and()
            .logout()
            .deleteCookies(securityRememberMeProps.getCookie())
            .logoutUrl(securityRequestMappingProps.getLogout())
            .logoutSuccessHandler(new RestLogoutSuccessHandler())
        .and()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .key(securityRememberMeProps.getKey())
        .and()
            .httpBasic().disable()
            .csrf().disable();
    }
}
