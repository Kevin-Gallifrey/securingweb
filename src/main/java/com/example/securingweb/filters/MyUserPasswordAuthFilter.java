package com.example.securingweb.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class MyUserPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {
    public MyUserPasswordAuthFilter() {
        // 拦截URL为"/login"的POST请求
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    /**
     * 覆写attemptAuthentication()
     * 从请求中取出用户名和密码，装入Authentication中
     * 并提交给AuthenticationManager处理
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        
        UsernamePasswordAuthenticationToken authRequst = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authRequst);
    }
}
