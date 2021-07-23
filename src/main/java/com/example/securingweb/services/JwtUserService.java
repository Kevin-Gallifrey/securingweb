package com.example.securingweb.services;

import com.example.securingweb.utils.JwtUtil;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtUserService implements UserDetailsService {

	private PasswordEncoder passwordEncoder;

	public JwtUserService() {
		// 使用默认的密码加密方式
		this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

    public String generateToken(UserDetails user) {
		String username = user.getUsername();
        String token = JwtUtil.sign(username);   	
        return token;
	}

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return User.builder().username(username).password(passwordEncoder.encode("password")).roles("USER").build();
	}
}
