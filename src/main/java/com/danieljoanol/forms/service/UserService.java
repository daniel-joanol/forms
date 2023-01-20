package com.danieljoanol.forms.service;

import java.time.LocalDate;

import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.entity.User;

public interface UserService extends GenericService<User> {
    
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    User updateNames(NamesUpdateRequest request);
    User updateLastPayment(Long id, LocalDate date);
    User updateComments(Long id, String comments);
    
}
