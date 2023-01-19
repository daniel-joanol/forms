package com.danieljoanol.forms.repository;

import java.util.Optional;

import com.danieljoanol.forms.entity.Role;

public interface RoleRepository extends GenericRepository<Role> {
 
    Optional<Role> findByName(String name);
}
