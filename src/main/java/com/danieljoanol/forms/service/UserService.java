package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.User;

public interface UserService extends GenericService<User> {
    
    User findByEmail(String email);
    Boolean existsByEmail(String email);
}
