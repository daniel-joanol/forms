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
    private Integer maxUsers;
    private Integer totalUsers;

    public RoleDTO(Role entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.maxUsers = entity.getTotalUsers();
        }
    }

    @Override
    public Role toEntity() {
        return Role.builder()
                .name(name)
                .maxUsers(maxUsers)
                .totalUsers(totalUsers)
                .build();
    }

}
