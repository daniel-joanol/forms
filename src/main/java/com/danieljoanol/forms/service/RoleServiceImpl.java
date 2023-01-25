package com.danieljoanol.forms.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.repository.RoleRepository;
import com.danieljoanol.forms.util.CodeGeneration;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Value("${forms.app.group}")
    private String group;

    @Override
    public Role createGroupRole() {
        String newName = null;
        boolean isUnique = true;
        Role role = null;
        
        do {
            newName = group + CodeGeneration.newCode();
            try {
                role = new Role();
                role.setName(newName);
                role = roleRepository.save(role);
            } catch (DataIntegrityViolationException e) {
                isUnique = false;
            }
        } while (isUnique = false);
        
        return role;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
    }

}
