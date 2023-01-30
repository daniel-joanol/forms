package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.User;

public interface ClientRepository extends GenericRepository<Client> {

    Page<Client> findByGroup_Users_UsernameInAndIsEnabledTrue(Pageable pageable, List<String> usernames);
    
    Optional<Client> findByIdAndGroup_Users_UsernameIn(Long id, List<String> usernames);

    List<Client> findByGroup_Users_UsernameIn(List<String> usernames);

    List<Client> findByGroup_UsersIn(List<User> users);

    Optional<Client> findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(Long id, List<String> usernames);

}
