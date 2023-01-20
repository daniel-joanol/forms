package com.danieljoanol.forms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieljoanol.forms.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
 
    Optional<Role> findByName(String name);
    
}
