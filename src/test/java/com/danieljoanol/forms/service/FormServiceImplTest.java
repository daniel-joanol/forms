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

import java.time.LocalDateTime;
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
import com.danieljoanol.forms.repository.FormRepository;

@ExtendWith(MockitoExtension.class)
public class FormServiceImplTest {
  
  @Mock
  private FormRepository formRepository;

  @Mock
  private ClientService clientService;

  @Mock
  private GroupService groupService;

  @InjectMocks
  private FormServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String STR_VALUE = "string";
  private static final Integer INT_VALUE = 10;
  private static final Long LONG_VALUE = 10l;

  @Test
  void whenCreate_thenReturnForm() {

    Client client = generator.nextObject(Client.class);
    Form form = generator.nextObject(Form.class);
    Group group = generator.nextObject(Group.class);
    client.setGroup(group);

    when(clientService.findByIdAndUsernames(anyLong(), anyList())).thenReturn(client);
    when(formRepository.save(any(Form.class))).thenReturn(form);

    final Form response = service.create(form, LONG_VALUE, STR_VALUE);
    assertEquals(form.getAgent(), response.getAgent());
    verify(clientService, times(1)).update(any(Client.class));
  }

  @Test
  void whenFindByUsernameAndFilters_thenReturnPage() {

    Group group = generator.nextObject(Group.class);
    List<Form> forms = generator.objects(Form.class, INT_VALUE).toList();
    Page<Form> page = new PageImpl<>(forms);

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(formRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    final Page<Form> response = service.findByUsernameAndFilters
        (INT_VALUE, INT_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, 
        STR_VALUE, STR_VALUE, STR_VALUE, LocalDateTime.MIN, LocalDateTime.MAX, true);
    assertEquals(forms.size(), response.getNumberOfElements());
    assertEquals(1, response.getTotalPages());
  }

  @Test
  void whenGet_thenReturnForm() {

    Form form = generator.nextObject(Form.class);

    when(formRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(form));

    final Form response = service.get(LONG_VALUE, STR_VALUE);
    assertEquals(form.getBrand(), response.getBrand());
  }

  @Test
  void whenGet_thenThrowException() {

    when(formRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class, 
        () -> service.get(LONG_VALUE, STR_VALUE));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenDelete_thenVerify() {

    Form form = generator.nextObject(Form.class);

    when(formRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(form));

    service.delete(LONG_VALUE, STR_VALUE);
    verify(formRepository, times(1)).delete(any(Form.class));
  }

  @Test
  void whenDeleteAll_thenVerify() {

    List<Form> forms = generator.objects(Form.class, 5).toList();

    service.deleteAll(forms);
    verify(formRepository, times(1)).deleteAll(anyList());
  }
  
  @Test
  void whenCloseOrOpenOrder_thenReturnForm() {

    Form form = generator.nextObject(Form.class);

    when(formRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(form));
    when(formRepository.save(any(Form.class))).thenReturn(form);

    final Form response = service.closeOrOpenOrder(LONG_VALUE, STR_VALUE, true);
    assertEquals(form.getComments(), response.getComments());
  }

  @Test
  void whenUpdate_thenReturnForm() {

    Form form = generator.nextObject(Form.class);

    when(formRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(form));
    when(formRepository.save(any(Form.class))).thenReturn(form);

    final Form response = service.update(form, STR_VALUE);
    assertEquals(form.getBrand(), response.getBrand());
  }

}
