package com.example.securingweb.config;

import com.example.securingweb.filters.MyUserPasswordAuthFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

public class LoginConfig<T extends LoginConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    
    private MyUserPasswordAuthFilter authFilter;
    
    public LoginConfig() {
        this.authFilter = new MyUserPasswordAuthFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        // 设置Filter使用的AuthenticationManager,这里取公共的即可
        authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 设置认证成功的Handler
        //authFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        // 设置失败的Handler
        authFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        
        MyUserPasswordAuthFilter filter = postProcess(authFilter);
        //指定Filter的位置
        http.addFilterAfter(filter, LogoutFilter.class);
    }    

    //设置成功的Handler
    public LoginConfig<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
        authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        return this;
    }

}
