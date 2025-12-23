package br.com.marcoshssilva.springadminserver.configs;

import br.com.marcoshssilva.springadminserver.data.JwtTokenResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import java.util.Objects;

public class CookieBearerTokenResolver implements BearerTokenResolver {
    private static final String COOKIE_NAME = "ACCESS_TOKEN";

    private final DefaultBearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest request) {
        String token = defaultResolver.resolve(request);
        if (token != null) {
            return token;
        }

        if (request.getCookies() == null) {
            return null;
        }

        JwtTokenResponse jwtToken = (JwtTokenResponse) request.getSession().getAttribute(COOKIE_NAME);
        if (Objects.nonNull(jwtToken)) {
            return jwtToken.getAccessToken();
        }

        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_NAME.equals(cookie.getName())) { return cookie.getValue(); }
        }

        return null;
    }
}
