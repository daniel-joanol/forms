package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.naming.AuthenticationException;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.AuthenticationRequest;
import com.danieljoanol.forms.controller.response.AuthenticationResponse;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
  
  @Mock
  private UserService userService;

  @Mock
  private TokenBlacklistService tokenBlacklistService;

  @Mock
  private AuthenticationManager authManager;

  @Mock
  private JwtTokenUtil jwtTokenUtil;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private AuthenticationServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String TOKEN = "809809809809809";

  @Test
  void whenLogin_thenReturnAuthenticationResponse() throws AuthenticationException {

    AuthenticationRequest request = generator.nextObject(AuthenticationRequest.class);
    
    User user = generator.nextObject(User.class);
    user.setEnabled(true);
    
    when(userService.findByUsername(anyString())).thenReturn(user);
    when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtTokenUtil.generateJwtToken(any(Authentication.class))).thenReturn(TOKEN);

    final AuthenticationResponse response = service.login(request);
    assertEquals(TOKEN, response.getToken());
    assertEquals(user.getFirstName(), response.getFirstName());
    assertEquals(user.getUsername(), response.getUsername());
  }

  @Test
  void whenLogin_thenThrowException() throws AuthenticationException {

    AuthenticationRequest request = generator.nextObject(AuthenticationRequest.class);
    
    User user = generator.nextObject(User.class);
    user.setEnabled(false);
    
    when(userService.findByUsername(anyString())).thenReturn(user);

    final AuthenticationException thrown = assertThrows(
        AuthenticationException.class, 
        () -> service.login(request));
    assertEquals(Message.userBlocked(request.getUsername()), thrown.getMessage());
  }

  @Test
  void whenLogout_thenReturnString() {
    
    final String response = service.logout(TOKEN);
    verify(tokenBlacklistService, times(1)).save(anyString());
    assertEquals(Message.LOG_OUT, response);
  }

}
