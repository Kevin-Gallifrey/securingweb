package com.example.securingweb.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.securingweb.config.JwtAuthenticationToken;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {
    private RequestMatcher AuthRequstMatcher;
    private List<RequestMatcher> permittedRequestMatchers;
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler;
	private AuthenticationFailureHandler failureHandler;

    public JwtAuthFilter() {
        this.AuthRequstMatcher = new RequestHeaderRequestMatcher("Authorization");
    }

    protected String getJwtToken(HttpServletRequest request) {
		String authInfo = request.getHeader("Authorization");
		return StringUtils.removeStart(authInfo, "Bearer ");
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //header??????token?????????????????????????????????url???????????????????????????
        //????????????????????????????????????????????????token????????????????????????????????????SecurityContext????????????????????????????????????????????????????????????
        if (!requiresAuthentication(request, response)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        Authentication authResult = null;
        AuthenticationException failed = null;

        try {
            //???????????????token?????????????????????AuthenticationManager
            String token = getJwtToken(request);
            if (StringUtils.isNotBlank(token)) {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(token);
                authResult = this.getAuthenticationManager().authenticate(authToken);
            } else {
                failed = new InsufficientAuthenticationException("JWT is Empty");
            }
        } catch (AuthenticationException e) {
            // Authentication failed
            failed = e;
        }

        if (authResult != null) {
            //token????????????
		    successfulAuthentication(request, response, filterChain, authResult);
		} else if(!permittedRequest(request)) {
			unsuccessfulAuthentication(request, response, failed);
			return;
		}

        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
	
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain, Authentication authResult) 
			throws IOException, ServletException{
		SecurityContextHolder.getContext().setAuthentication(authResult);
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}
    
    protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		return AuthRequstMatcher.matches(request);
	}

    protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

    protected boolean permittedRequest(HttpServletRequest request) {
		if(permittedRequestMatchers == null)
			return false;
		for(RequestMatcher permittedMatcher : permittedRequestMatchers) {
			if(permittedMatcher.matches(request))
				return true;
		}		
		return false;
	}

    public void setPermissiveUrls(String... urls) {
		if(permittedRequestMatchers == null)
			permittedRequestMatchers = new ArrayList<>();
		for(String url : urls)
			permittedRequestMatchers.add(new AntPathRequestMatcher(url));
	}
}
