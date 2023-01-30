package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.User;

public interface ClientRepository extends GenericRepository<Client> {
    
    Optional<Client> findByIdAndGroup_Users_UsernameIn(Long id, List<String> usernames);

    List<Client> findByGroup_Users_UsernameIn(List<String> usernames);

    List<Client> findByGroup_UsersIn(List<User> users);

}
