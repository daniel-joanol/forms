package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.User;

public interface UserService extends GenericService<User> {
    
    User findByUsername(String username);
    Boolean existsByUsername(String username);
}
