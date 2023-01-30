package com.danieljoanol.forms.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.repository.GroupRepository;
import com.danieljoanol.forms.util.CodeGeneration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
    
    private final GroupRepository groupRepository;

    @Value("${forms.app.group}")
    private String GROUP_PREFIX;

    @Override
    public Group create(Integer max) throws Exception {
        String newName = null;
        boolean isUnique = true;
        int tries = 0;
        Group group = null;

        do {
            newName = GROUP_PREFIX + CodeGeneration.newCode();
            try {
                isUnique = true;
                group = Group.builder()
                        .name(newName)
                        .maxUsers(max)
                        .totalUsers(1)
                        .users(new ArrayList<User>())
                        .build();
                group = groupRepository.save(group);
            } catch (DataIntegrityViolationException ex) {
                log.error(ex.getMessage(), ex);
                isUnique = false;
                tries++;
            }
        } while (!isUnique && tries < 5);

        if (group == null) {
            throw new Exception(Message.GENERIC_ERROR);
        }

        return group;
    }

    @Override
    public Group updateMaxUsers(Long id, Integer maxUsers) throws UsersLimitException {
        Group group = get(id);
        
        if (maxUsers < group.getTotalUsers()) {
            throw new UsersLimitException(Message.MAX_USERS_ERROR_ON_UPDATE);
        }

        group.setMaxUsers(maxUsers);
        return groupRepository.save(group);
    }

    @Override
    public Page<Group> getAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return groupRepository.findAll(pageable);
    }

    @Override
    public Group get(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Message.ID_NOT_FOUND));
    }

    @Override
    public Group getByUserIn(User user) throws AccessDeniedException {
        return groupRepository.findByUsersIn(List.of(user))
                .orElseThrow(() -> new AccessDeniedException(Message.NOT_AUTHORIZED));
    }

    @Override
    public Group getByUsernameIn(List<String> usernames) throws AccessDeniedException {
        return groupRepository.findByUsers_UsernameIn(usernames)
                .orElseThrow(() -> new AccessDeniedException(Message.NOT_AUTHORIZED));
    }

    @Override
    public Group update(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public void delete(Group group) {
        groupRepository.delete(group);
    }

}
