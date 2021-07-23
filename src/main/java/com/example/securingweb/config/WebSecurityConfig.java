package com.example.securingweb.config;

import com.example.securingweb.services.JwtAuthProvider;
import com.example.securingweb.services.JwtUserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.disable()
			.csrf()
				.disable()
			/*
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			*/
			//添加登录filter
			.apply(new LoginConfig<>())
				.loginSuccessHandler(myLoginSuccessHandler())
				.and()
			//添加token的filter
			.apply(new JwtLoginConfig<>())
				.tokenValidSuccessHandler(myJwtAuthSuccessHandler())
				.permittedRequstUrls("/", "/home", "/login", "/jdbctest")
				.and()	
			.logout()
				.permitAll();
	}

	// 配置provider
	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception{
		builder.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
	}

	@Bean("daoAuthenticationProvider")
	protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService());
		return daoProvider;
	}

	@Bean("jwtAuthenticationProvider")
	protected AuthenticationProvider jwtAuthenticationProvider() {
		return new JwtAuthProvider(jwtUserService());
	}

	@Bean
	protected LoginSuccessHandler myLoginSuccessHandler() {
		return new LoginSuccessHandler(jwtUserService());
	}

	@Bean
	protected JwtAuthSuccessHandler myJwtAuthSuccessHandler() {
		return new JwtAuthSuccessHandler();
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return new JwtUserService();
	}
	
	@Bean("jwtUserService")
	protected JwtUserService jwtUserService() {
		return new JwtUserService();
	}
	
}