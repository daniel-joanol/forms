package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;

public interface UserRepository extends GenericRepository<User> {
    
    Optional<User> findByUsername(String username);
    
    Boolean existsByUsername(String username);

    List<User> findByRolesIn(Role role);

}
