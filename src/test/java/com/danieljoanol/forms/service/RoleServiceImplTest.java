package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
  
  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String NAME = "name";

  @Test
  void whenFindByName_thenReturnRole() {

    Role role = generator.nextObject(Role.class);

    when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

    final Role response = service.findByName(NAME);
    assertEquals(role.getName(), response.getName());
  }

  @Test
  void whenFindByName_thenThrowException() {

    when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
      EntityNotFoundException.class,
      () -> service.findByName(NAME));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

}
