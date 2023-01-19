package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Role;

public interface RoleService extends GenericService<Role> {
    
    Role findByName(String name);
}
