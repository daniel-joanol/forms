package com.danieljoanol.forms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerController {
    
    @ExceptionHandler(value = { UsernameNotFoundException.class, DuplicateKeyException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUsernameNotFound(HttpServletRequest request, Exception ex) {
        String errorMessage = getErrorMessage(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    private String getErrorMessage(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
