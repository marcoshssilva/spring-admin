package com.github.marcoshssilva.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth2/login")
public class LoginController {

    @Value("${spring.security.oauth2.resourceserver.jwt.authorization_endpoint}")
    private String oauth2AuthorizeUrl;

    @Value("${spring.security.oauth2.resourceserver.jwt.redirect_uri}")
    private String oauth2RedirectUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.client_id}")
    private String oauth2ClientID;

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    private String oauth2Audience;

    @GetMapping
    public String doLogin(){
        return "redirect:".concat(this.oauth2AuthorizeUrl)
                .concat("?response_type=code")
                .concat("&client_id=").concat(this.oauth2ClientID)
                .concat("&redirect_uri=").concat(this.oauth2RedirectUri)
                .concat("&audience=").concat(this.oauth2Audience)
                .concat("&state=login")
                .concat("&scope=profile");
    }
}
