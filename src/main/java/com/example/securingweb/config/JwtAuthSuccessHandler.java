package com.example.securingweb.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.securingweb.services.LogInfoService;
import com.example.securingweb.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JwtAuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private LogInfoService logInfoService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String jwt = ((JwtAuthenticationToken)authentication).getToken();
        String user = JwtUtil.getUserNameByToken(jwt);
        String status = "JWT authentication succeeded";
        
        // 在session中保存用户名
        request.getSession().setAttribute("username", user);

        // 将认证信息写入数据库
        logInfoService.insertLogInfo(logInfoService.createLogIn(request, user, status));
        System.out.println(status);
    }
}
