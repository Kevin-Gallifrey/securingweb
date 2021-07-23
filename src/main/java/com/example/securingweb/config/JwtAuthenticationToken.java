package com.example.securingweb.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private UserDetails principal;
    private String credentials;
    private String jwt;
    
    public JwtAuthenticationToken(String jwt) {
        super(Collections.emptyList());
        this.jwt = jwt;
    }

    public JwtAuthenticationToken(UserDetails principal, String jwt, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.jwt = jwt;
	}

    @Override
	public void setDetails(Object details) {
		super.setDetails(details);
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

    public String getToken() {
		return jwt;
	}
}
