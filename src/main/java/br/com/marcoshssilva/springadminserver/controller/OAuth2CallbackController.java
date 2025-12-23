package br.com.marcoshssilva.springadminserver.controller;

import br.com.marcoshssilva.springadminserver.data.JwtTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Controller
@RequestMapping(path = "/api/oauth2/callback")
public class OAuth2CallbackController {

    @Value("${spring.security.oauth2.resourceserver.jwt.token_endpoint}")
    String oauth2TokenEndpoint;

    @Value("${spring.security.oauth2.resourceserver.jwt.client_id}")
    String oauth2ClientID;

    @Value("${spring.security.oauth2.resourceserver.jwt.client_secret}")
    String oauth2ClientSecret;

    @Value("${spring.security.oauth2.resourceserver.jwt.redirect_uri}")
    private String oauth2TokenRedirectUri;

    @GetMapping
    public String callback(@RequestParam(required = false) String code, HttpServletRequest httpServletRequest) {
        JwtTokenResponse res = null;

        if (Objects.nonNull(code)) {
            String base64Auth = Base64.getEncoder().encodeToString((oauth2ClientID + ":" + oauth2ClientSecret).getBytes(StandardCharsets.UTF_8));
            WebClient client = WebClient.builder()
                    .baseUrl(oauth2TokenEndpoint)
                    .defaultHeader("Authorization", "Basic ".concat(base64Auth))
                    .build();

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "authorization_code");
            formData.add("client_id", oauth2ClientID);
            formData.add("client_secret", oauth2ClientSecret);
            formData.add("code", code);
            formData.add("redirect_uri", oauth2TokenRedirectUri);

            ResponseEntity<JwtTokenResponse> response = client.post()
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .toEntity(JwtTokenResponse.class)
                    .block();

            if (Objects.nonNull(response) && response.getStatusCode().is2xxSuccessful()) {
                res = response.getBody();
                httpServletRequest.getSession().setAttribute("ACCESS_TOKEN", res);
            }
        }

        return "redirect:/";
    }

}
