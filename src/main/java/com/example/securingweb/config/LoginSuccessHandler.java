package com.example.securingweb.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.securingweb.services.JwtUserService;
import com.example.securingweb.services.LogInfoService;
import com.example.securingweb.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{
	@Autowired
    private LogInfoService logInfoService;
	
    private JwtUserService jwtUserService;

	public LoginSuccessHandler(JwtUserService jwtUserService) {
        this.jwtUserService = jwtUserService;
    }

    @Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String token = jwtUserService.generateToken((UserDetails)authentication.getPrincipal());
		response.setHeader("Authorization", "Bearer " + token);

		String user = JwtUtil.getUserNameByToken(token);
        String status = "Login succeeded";
        
        logInfoService.insertLogInfo(logInfoService.createLogIn(request, user, status));
        System.out.println(status);
        System.out.println("Send token: " + token);
	}
}
