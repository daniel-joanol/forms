package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.UsersLimitException;

public interface GroupService {
    
    public Page<Group> getAll(Integer pageNumber, Integer pageSize);

    public Group get(Long id);

    public Group getByUserIn(List<User> users) throws AccessDeniedException;

    public Group getByUsernameIn(List<String> username) throws AccessDeniedException;

    public Group create(Integer max) throws Exception;

    public Group updateMaxUsers(Long id, Integer maxUsers) throws UsersLimitException;

    public Group update(Group group);
    
    public void delete(Group group);
    
}
