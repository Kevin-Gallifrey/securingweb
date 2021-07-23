package com.example.securingweb.config;

import com.example.securingweb.filters.JwtAuthFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class JwtLoginConfig<T extends JwtLoginConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    private JwtAuthFilter authFilter;
    
    public JwtLoginConfig() {
        this.authFilter = new JwtAuthFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		authFilter.setAuthenticationFailureHandler(new JwtAuthFailureHandler());
        
        //将filter放到logoutFilter之前
		JwtAuthFilter filter = postProcess(authFilter);
		http.addFilterBefore(filter, LogoutFilter.class);
    }

    public JwtLoginConfig<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler){
        authFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }

    public JwtLoginConfig<T, B> permittedRequstUrls(String... urls) {
        authFilter.setPermissiveUrls(urls);
        return this;
    }
}
