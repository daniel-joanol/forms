package com.danieljoanol.forms.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.danieljoanol.forms.entity.Group;
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
public class GroupDTO extends GenericDTO<Group> {
    
    @NotNull(message = "id {err.null}")
    private Long id;

    @NotBlank(message = "name {err.blank}")
    private String name;

    @NotNull(message = "maxUsers {err.null}")
    @Min(value = 1)
    private Integer maxUsers;

    private Integer totalUsers;

    public GroupDTO(Group entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.maxUsers = entity.getMaxUsers();
            this.totalUsers = entity.getTotalUsers();
        }
    }

    @Override
    public Group toEntity() {
        return Group.builder()
                .id(id)
                .name(name)
                .maxUsers(maxUsers)
                .totalUsers(totalUsers)
                .build();
    }
}
