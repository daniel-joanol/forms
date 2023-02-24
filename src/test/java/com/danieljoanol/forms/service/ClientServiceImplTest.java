package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.repository.FormRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

  @Mock
  private ClientRepository clientRepository;

  @Mock
  private GroupService groupService;

  @Mock
  private FormRepository formRepository;

  @InjectMocks
  private ClientServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String STR_VALUE = "string";
  private static final Integer INT_VALUE = 10;
  private static final Long LONG_VALUE = 10l;

  @Test
  void whenCreate_thenReturnClient() {

    Client client = generator.nextObject(Client.class);
    Group group = generator.nextObject(Group.class);

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(clientRepository.save(any(Client.class))).thenReturn(client);

    final Client response = service.create(client, STR_VALUE);
    assertEquals(client.getName(), response.getName());
    assertEquals(client.getGroup().getMaxUsers(), response.getGroup().getMaxUsers());
  }

  @Test
  void whenFindByUsernameAndFilters_thenReturnPage() {

    Group group = generator.nextObject(Group.class);
    List<Client> clients = generator.objects(Client.class, INT_VALUE).toList();
    Page<Client> page = new PageImpl<>(clients);

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    final Page<Client> response = service.findByUsernameAndFilters(INT_VALUE, INT_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE);
    assertEquals(clients.size(), response.getNumberOfElements());
    assertEquals(1, response.getTotalPages());
  }

  @Test
  void whenFindByIdAndUsernames_thenReturnClient() {

    Client client = generator.nextObject(Client.class);
    List<String> usernames = List.of("username1", "username2");

    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(client));

    final Client response = service.findByIdAndUsernames(LONG_VALUE, usernames);
    assertEquals(client.getAddress(), response.getAddress());
  }

  @Test
  void whenFindByIdAndUsernames_thenThrowException() {

    List<String> usernames = List.of("username1", "username2");

    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class,
        () -> service.findByIdAndUsernames(LONG_VALUE, usernames));
    assertEquals(Message.NOT_AUTHORIZED, thrown.getMessage());
  }

  @Test
  void whenFindAllByUser_thenReturnList() {

    User user = generator.nextObject(User.class);
    List<Client> clients = generator.objects(Client.class, 5).toList();

    when(clientRepository.findByGroup_UsersIn(anyList())).thenReturn(clients);

    final List<Client> response = service.findAllByUser(user);
    assertEquals(clients.size(), response.size());
  }

  @Test
  void whenFinalAllByUsername_thenReturnList() {

    String username = "username";
    List<Client> clients = generator.objects(Client.class, 5).toList();

    when(clientRepository.findByGroup_Users_UsernameIn(anyList())).thenReturn(clients);

    final List<Client> response = service.findAllByUsername(username);
    assertEquals(clients.size(), response.size());
  }

  @Test
  void whenGet_thenReturnClient() {

    String username = "username";
    Client client = generator.nextObject(Client.class);

    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(client));

    final Client response = service.get(LONG_VALUE, username);
    assertEquals(client.getName(), response.getName());
  }

  @Test
  void whenGet_thenThrowException() {

    String username = "username";
    
    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
      EntityNotFoundException.class,
      () -> service.get(LONG_VALUE, username)
    );
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenUpdate_thenReturnClient() {

    Client client = generator.nextObject(Client.class);

    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(client));
    when(clientRepository.save(any(Client.class))).thenReturn(client);

    final Client response = service.update(client, STR_VALUE);
    assertEquals(client.getName(), response.getName());
  }

  @Test
  void whenDeleteAll_thenVerify() {

    List<Client> clients = generator.objects(Client.class, 5).toList();

    service.deleteAll(clients);
    verify(clientRepository, times(1)).deleteAll(anyList());
  }

  @Test
  void whenDelete_thenVerify() {

    Client client = generator.nextObject(Client.class);
    List<Form> forms = generator.objects(Form.class, 5).toList();
    client.setForms(forms);

    when(clientRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(client));

    service.delete(LONG_VALUE, STR_VALUE);
    verify(clientRepository, times(1)).delete(any(Client.class));
    verify(formRepository, times(1)).deleteAll(anyList());
  }

}
