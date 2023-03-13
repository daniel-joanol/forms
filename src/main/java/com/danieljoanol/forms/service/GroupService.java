package com.danieljoanol.forms.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.exception.UsersLimitException;

public interface GroupService {

  public Page<Group> getAll(Integer pageNumber, Integer pageSize, String name, Integer maxUsers, Integer totalUsers,
      String username);

  public Group get(Long id);

  public Group getByUsername(String username) throws EntityNotFoundException;

  public Group create(Integer max, String prefix) throws Exception;

  public Group updateMaxUsers(Long id, Integer maxUsers) throws UsersLimitException;

  public Group update(Group group);

  public void delete(Group group);

  public void deleteGroups(List<Group> groups);

}
