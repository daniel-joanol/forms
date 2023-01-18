package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.enums.ERole;

public interface RoleService extends GenericService<Role> {
    
    Role findByName(ERole name);
}
