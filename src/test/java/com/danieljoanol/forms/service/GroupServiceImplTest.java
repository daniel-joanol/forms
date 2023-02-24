package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.repository.GroupRepository;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {
  
  @Mock
  private GroupRepository groupRepository;

  @InjectMocks
  private GroupServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String STR_VALUE = "string";
  private static final Integer INT_VALUE = 10;
  private static final Long LONG_VALUE = 10l;

  @Test
  void whenCreate_thenReturnGroup() throws Exception {

    Group group = generator.nextObject(Group.class);

    when(groupRepository.save(any(Group.class))).thenReturn(group);

    final Group response = service.create(INT_VALUE, STR_VALUE);
    assertEquals(group.getName(), response.getName());
  }

  @Test
  void whenCreate_thenThrowException() throws Exception {

    when(groupRepository.save(any(Group.class))).thenThrow(new DataIntegrityViolationException(STR_VALUE));

    final Exception thrown = assertThrows(
        Exception.class,
        () -> service.create(INT_VALUE, STR_VALUE));
    assertEquals(Message.GENERIC_ERROR, thrown.getMessage());
  }

  @Test
  void whenUpdateMaxUsers_thenReturnGroup() throws UsersLimitException {

    Group group = generator.nextObject(Group.class);
    group.setTotalUsers(1);

    when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
    when(groupRepository.save(any(Group.class))).thenReturn(group);

    final Group response = service.updateMaxUsers(LONG_VALUE, INT_VALUE);
    assertEquals(group.getName(), response.getName());
  }

  @Test
  void whenUpdateMaxUsers_thenThrowException() throws UsersLimitException {

    Group group = generator.nextObject(Group.class);
    group.setTotalUsers(20);

    when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

    final UsersLimitException thrown = assertThrows(
        UsersLimitException.class,
        () -> service.updateMaxUsers(LONG_VALUE, INT_VALUE));
    assertEquals(Message.MAX_USERS_ERROR_ON_UPDATE, thrown.getMessage());
  }

  @Test
  void whenGetAll_thenReturnPage() {

    List<Group> groups = generator.objects(Group.class, INT_VALUE).toList();
    Page<Group> page = new PageImpl<>(groups);

    when(groupRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    final Page<Group> response = service.getAll(INT_VALUE, INT_VALUE, STR_VALUE, INT_VALUE, INT_VALUE, STR_VALUE);
    assertEquals(groups.size(), response.getNumberOfElements());
    assertEquals(1, response.getTotalPages());
  }

  @Test
  void whenGet_thenReturnGroup() {

    Group group = generator.nextObject(Group.class);

    when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));

    final Group response = service.get(LONG_VALUE);
    assertEquals(group.getName(), response.getName());
  }

  @Test
  void whenGet_thenThrowException() {

    when(groupRepository.findById(anyLong())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class,
        () -> service.get(LONG_VALUE));
    assertEquals(Message.ID_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenGetByUsername_thenReturnGroup() {

    Group group = generator.nextObject(Group.class);

    when(groupRepository.findByUsers_UsernameIn(anyList())).thenReturn(Optional.of(group));

    final Group response = service.getByUsername(STR_VALUE);
    assertEquals(group.getName(), response.getName());
  }

  @Test
  void whenGetByUsername_thenThrowException() {

    when(groupRepository.findByUsers_UsernameIn(anyList())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class,
        () -> service.getByUsername(STR_VALUE));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenUpdate_thenReturn() {

    Group group = generator.nextObject(Group.class);

    when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
    when(groupRepository.save(any(Group.class))).thenReturn(group);

    final Group response = service.update(group);
    assertEquals(group.getName(), response.getName());
  }

  @Test
  void whenDelete_thenVerify() {

    Group group = generator.nextObject(Group.class);

    service.delete(group);
    verify(groupRepository, times(1)).delete(any(Group.class));
  }

  @Test
  void whenDeleteGroups_thenVerify() {

    List<Group> groups = generator.objects(Group.class, 5).toList();

    service.deleteGroups(groups);
    verify(groupRepository, times(1)).deleteAll(anyList());
  }
}
