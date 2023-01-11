package com.danieljoanol.forms.repository;

import java.util.Optional;

import com.danieljoanol.forms.entity.User;

public interface UserRepository extends GenericRepository<User> {
    
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

}
