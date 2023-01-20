package com.danieljoanol.forms.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.danieljoanol.forms.constants.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {@Override
    
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ex) throws IOException, ServletException {
        
        log.error("Unauthorized error: {}", ex.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Message.notAuthorized());
        
    }

}
