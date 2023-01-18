package com.danieljoanol.forms.repository;

import java.util.Optional;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.enums.ERole;

public interface RoleRepository extends GenericRepository<Role> {
 
    Optional<Role> findByName(ERole name);
}
