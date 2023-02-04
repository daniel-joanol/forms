package com.danieljoanol.forms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;

public interface UserRepository extends GenericRepository<User> {
    
    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndIsEnabledTrue(Long id);
    
    Boolean existsByUsername(String username);

    List<User> findByRolesIn(List<Role> roles);

    List<User> findByRoles_NameIn(List<String> roles);

    List<User> findByIsEnabledFalseAndDisabledDateLessThan(LocalDate date);
    


}
