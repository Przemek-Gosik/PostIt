package com.example.backend.config;

import com.example.backend.constants.TokenConstants;
import com.example.backend.model.User;
import com.example.backend.utilis.TokenCreator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final TokenCreator tokenCreator;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenCreator tokenCreator,
                                   UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.tokenCreator = tokenCreator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException {
        try{
            UsernamePasswordAuthenticationToken passwordAuthenticationToken = authenticateUser(request);
            SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
            chain.doFilter(request,response);

        } catch (Exception e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }

    private UsernamePasswordAuthenticationToken authenticateUser(HttpServletRequest request) {
        String token = request.getHeader(TokenConstants.HEADER);
        if(!token.isEmpty() && token.startsWith(TokenConstants.PREFIX)){
            String login = tokenCreator.getLoginFromToken(token);
            if(login.isEmpty()){
                throw new AuthenticationCredentialsNotFoundException("No login provided in token!");
            }else{
                User user = (User) userDetailsService.loadUserByUsername(login);
                 return new UsernamePasswordAuthenticationToken(user,null,null);
            }
        }else{
            throw new AuthenticationCredentialsNotFoundException("No token provided, or without right prefix!");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return request.getRequestURI().equals("/api/auth/login") ||
                request.getRequestURI().equals("/api/auth/register");
    }

}
