package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.GroupDTO;
import com.danieljoanol.forms.entity.Group;

@Component
public class GroupAssembler extends GenericAssembler<Group, GroupDTO> {
    
    @Override
    public GroupDTO convertToDTO(Group entity) {
        return new GroupDTO(entity);
    }

}
