package com.danieljoanol.forms.service;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.exception.UsersLimitException;

public interface RoleService {
    
    public Page<Role> getAll(Integer pageNumber, Integer pageSize);

    public Role get(Long id);

    public Role createGroupRole(Integer max) throws Exception;
    
    public Role findByName(String name);
    
    public Role update(Role role);

    public Role updateMaxUsers(Long id, Integer maxUsers) throws UsersLimitException;

    public void delete(Role role);

}
