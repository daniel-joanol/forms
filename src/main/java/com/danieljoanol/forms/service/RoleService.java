package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Role;

public interface RoleService {
    
    Role findByName(String name);
}
