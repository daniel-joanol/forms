package com.danieljoanol.forms.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.enums.ERole;
import com.danieljoanol.forms.repository.RoleRepository;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(ERole name) {
        return roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

}
