package com.danieljoanol.forms.exception;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerController {

    @ExceptionHandler(value = { EntityNotFoundException.class, UsernameNotFoundException.class,
            DuplicateKeyException.class, Exception.class, CodeException.class, NoParentException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUsernameNotFound(HttpServletRequest request, Exception ex) {
        String errorMessage = getErrorMessage(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/text")
                .body(errorMessage);
    }

    @ExceptionHandler(value = { AuthenticationException.class, AccessDeniedException.class,
            BadCredentialsException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationEx(HttpServletRequest request, Exception ex) {
        String errorMessage = getErrorMessage(ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header("Content-Type", "application/text")
                .body(errorMessage);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(HttpServletRequest request,
            MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        String messageError = ex.getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Content-Type", "application/text")
                .body(messageError);
    }

    private String getErrorMessage(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

}
