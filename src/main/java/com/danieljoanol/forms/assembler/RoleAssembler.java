package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.RoleDTO;
import com.danieljoanol.forms.entity.Role;

@Component
public class RoleAssembler extends GenericAssembler<Role, RoleDTO> {

    @Override
    public RoleDTO convertToDTO(Role entity) {
        return new RoleDTO(entity);
    }
    
}
