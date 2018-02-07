package com.example.demo.security;

import org.springframework.security.authentication.AuthenticationManager;

public class JwtAuthenticationTokenFilter {
    private AuthenticationManager authentificationManager;
    private JwtSuccessHandler authentificationSuccessHandler;

    public void setAuthentificationManager(AuthenticationManager authentificationManager) {
        this.authentificationManager = authentificationManager;
    }

    public void setAuthentificationSuccessHandler(JwtSuccessHandler authentificationSuccessHandler) {
        this.authentificationSuccessHandler = authentificationSuccessHandler;
    }
}
