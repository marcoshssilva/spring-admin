package br.com.marcoshssilva.springadminserver.configs;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.ForwardLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

@Configuration
@EnableMethodSecurity()
@EnableWebSecurity

@lombok.extern.slf4j.Slf4j
@lombok.RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final AdminServerProperties adminServer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String loginPageUrl = this.adminServer.getContextPath() + "/login";

        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");

        http.authorizeHttpRequests(req ->
                req.requestMatchers(loginPageUrl,
                            this.adminServer.getContextPath() + "/assets/**",
                            this.adminServer.getContextPath() + "/actuator/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage(loginPageUrl)
                        .successHandler(successHandler))
                .logout(logout -> logout
                          .logoutUrl(this.adminServer.getContextPath() + "/logout")
                          .logoutSuccessHandler(
                                new ForwardLogoutSuccessHandler(loginPageUrl)))
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher(this.adminServer.getContextPath() + "/logout", HttpMethod.POST.toString())))
                .rememberMe(rememberMe -> rememberMe.key(UUID.randomUUID()
                                .toString())
                        .tokenValiditySeconds(1209600));
        return http.build();
    }
}
