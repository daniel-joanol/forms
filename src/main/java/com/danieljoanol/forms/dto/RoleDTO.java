package com.danieljoanol.forms.dto;

import com.danieljoanol.forms.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO extends GenericDTO<Role> {

    private long id;
    private String name;

    public RoleDTO(Role entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }

    @Override
    public Role toEntity() {
        Role entity = new Role();
        entity.setId(this.id);
        entity.setName(this.name);
        return entity;
    }

}
