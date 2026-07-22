package com.github.marcoshssilva.springadmin.configs;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity()
@EnableWebSecurity

@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final AdminServerProperties adminServer;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return new CookieBearerTokenResolver();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, BearerTokenResolver bearerTokenResolver) {
        String[] publicUrls = new String[] {
                "/actuator/**",
                "/api/oauth2/callback",
                "/api/oauth2/login",
                this.adminServer.getContextPath().concat("/assets/**"),
                this.adminServer.getContextPath().concat("/logout")
        };

        // enable oauth2 resource server
        http.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver).jwt(Customizer.withDefaults()));

        // use default csrf
        http.csrf(Customizer.withDefaults());

        // restrict routes
        http.authorizeHttpRequests(req -> req.requestMatchers(publicUrls).permitAll().anyRequest().authenticated());

        // redirect to login page from oauth2 issuer if not authenticated
        http.formLogin(formLogin -> formLogin.loginPage("/api/oauth2/login"));

        // clear all authentication when call logout
        http.logout(logout -> logout.deleteCookies("JSESSIONID").invalidateHttpSession(Boolean.TRUE).clearAuthentication(Boolean.TRUE).logoutUrl(this.adminServer.getContextPath().concat("/logout")));

        return http.build();
    }
}
