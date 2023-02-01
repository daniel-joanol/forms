package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    
    Optional<Group> findByUsers_UsernameIn(List<String> username);

    Optional<Group> findByUsersIn(List<User> users);

}
