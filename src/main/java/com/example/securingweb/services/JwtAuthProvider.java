package com.example.securingweb.services;

import com.example.securingweb.config.JwtAuthenticationToken;
import com.example.securingweb.utils.JwtUtil;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthProvider implements AuthenticationProvider {
    private JwtUserService jwtUserService;

    public JwtAuthProvider(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String jwt = ((JwtAuthenticationToken)authentication).getToken();
        String username = JwtUtil.getUserNameByToken(jwt);
        UserDetails user = jwtUserService.loadUserByUsername(username);
        try {
            JwtUtil.verify(jwt, username);
        } catch (Exception exception) {
            throw new BadCredentialsException("JWT token verify fail", exception);
        }
        
        //认证成功后返回认证信息，filter会将认证信息放入SecurityContext
        JwtAuthenticationToken token = new JwtAuthenticationToken(user, jwt, user.getAuthorities());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }
}
