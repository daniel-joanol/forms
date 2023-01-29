package com.danieljoanol.forms.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.repository.RoleRepository;
import com.danieljoanol.forms.util.CodeGeneration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Value("${forms.app.group}")
    private String group;

    @Override
    public Page<Role> getAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role get(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Message.ID_NOT_FOUND));
    }

    @Override
    public Role createGroupRole(Integer max) throws Exception {
        String newName = null;
        boolean isUnique = true;
        int tries = 0;
        Role role = null;

        do {
            newName = group + CodeGeneration.newCode();
            try {
                isUnique = true;
                role = new Role();
                role.setName(newName);
                role.setMaxUsers(max);
                role.setTotalUsers(1);
                role = roleRepository.save(role);
            } catch (DataIntegrityViolationException ex) {
                log.error(ex.getMessage(), ex);
                isUnique = false;
                tries++;
            }
        } while (!isUnique && tries < 5);

        if (role == null) {
            throw new Exception(Message.GENERIC_ERROR);
        }

        return role;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));

    }

    @Override
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role updateMaxUsers(Long id, Integer maxUsers) throws UsersLimitException {
        Role role = get(id);
        if (maxUsers < role.getTotalUsers()) {
            throw new UsersLimitException(Message.MAX_USERS_ERROR_ON_UPDATE);
        }

        role.setMaxUsers(maxUsers);
        return roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

}
