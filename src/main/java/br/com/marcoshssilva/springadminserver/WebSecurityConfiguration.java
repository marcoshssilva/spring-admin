package br.com.marcoshssilva.springadminserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity()
@EnableWebSecurity

@lombok.extern.slf4j.Slf4j
public class WebSecurityConfiguration {

    static final String[] AUTH_WHITELIST = { "/actuator/**" };

    @Bean
    public SecurityFilterChain securityFilterChainConfigure(HttpSecurity http) throws Exception {
        log.info("Configured with enabled Cross-Origin-Resource-Sharing");
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));
        log.info("Configured with disabled Csrf Tokens");
        http.csrf(CsrfConfigurer::disable);
        log.info("Configured with disabled Sessions");
        http.sessionManagement(sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        log.info("Configured with enabled path only to authenticated users");
        http.authorizeHttpRequests(httpRequestsConfigurer -> httpRequestsConfigurer.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated());
        log.info("Configured with enabled authentication using Http Basic Auth");
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        log.info("Setup cors with fully enabled Origins, Methods and Headers");
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}
