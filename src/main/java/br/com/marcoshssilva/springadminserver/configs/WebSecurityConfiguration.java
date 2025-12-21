package br.com.marcoshssilva.springadminserver.configs;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.oauth2.resourceserver.jwt.authorization_endpoint}")
    private String oauth2AuthorizeUrl;

    @Value("${spring.security.oauth2.resourceserver.jwt.redirect_uri}")
    private String oauth2RedirectUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.client_id}")
    private String oauth2ClientID;

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String oauth2Audience;

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
    public SecurityFilterChain filterChain(HttpSecurity http, BearerTokenResolver bearerTokenResolver) throws Exception {
        String[] publicUrls = new String[] { "/api/oauth2/callback", "/api/oauth2/callback/token", "/actuator/**", this.adminServer.getContextPath().concat("/assets/**"), this.adminServer.getContextPath().concat("/logout") };

        // enable oauth2 resource server
        http.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver).jwt(Customizer.withDefaults()));

        // use default csrf
        http.csrf(Customizer.withDefaults());

        // restrict routes
        http.authorizeHttpRequests(req -> req.requestMatchers(publicUrls).permitAll().anyRequest().authenticated());

        // redirect to login page from oauth2 issuer if not authenticated
        http.formLogin(formLogin -> formLogin.loginPage(
                this.oauth2AuthorizeUrl.concat("?response_type=code")
                        .concat("&client_id=").concat(this.oauth2ClientID)
                        .concat("&redirect_uri=").concat(this.oauth2RedirectUri)
                        .concat("&audience=").concat(this.oauth2Audience)
                        .concat("&state=login")
                        .concat("&scope=profile")
        ));

        // clear all authentication when call logout
        http.logout(logout -> logout
                .deleteCookies("JSESSIONID", "ACCESS_TOKEN")
                .invalidateHttpSession(Boolean.TRUE)
                .clearAuthentication(Boolean.TRUE)
                .logoutUrl(this.adminServer.getContextPath().concat("/logout"))
        );

        return http.build();
    }
}
