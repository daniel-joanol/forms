package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Role;

public interface RoleService {
    
    public Role createGroupRole(Integer max) throws Exception;
    
    public Role findByName(String name);
    
    public Role update(Role role);

}
