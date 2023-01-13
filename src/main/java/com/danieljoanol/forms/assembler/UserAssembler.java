package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.UserDTO;
import com.danieljoanol.forms.entity.User;

@Component
public class UserAssembler extends GenericAssembler<User, UserDTO> {
    
    @Override
    public UserDTO convertToDTO(User entity) {
        return new UserDTO(entity);
    }

}
