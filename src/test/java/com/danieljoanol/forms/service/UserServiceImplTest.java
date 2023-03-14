package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.request.user.CodeConfirmationRequest;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.controller.request.user.PasswordUpdateRequest;
import com.danieljoanol.forms.controller.request.user.UsernameUpdateRequest;
import com.danieljoanol.forms.email.SparkPostService;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.CodeException;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.repository.UserRepository;
import com.danieljoanol.forms.repository.specification.UserSpecification;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.util.CodeGeneration;
import com.sparkpost.exception.SparkPostException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private SparkPostService sparkPostService;

  @Mock
  private PasswordEncoder encoder;

  @Mock
  private RoleService roleService;

  @Mock
  private ShopService shopService;

  @Mock
  private ClientService clientService;

  @Mock
  private FormService formService;

  @Mock
  private GroupService groupService;

  @InjectMocks
  private UserServiceImpl userService;

  private static final EasyRandom generator = new EasyRandom();
  private static final String STR_VALUE = "string";
  private static final Integer INT_VALUE = 10;
  private static final Long LONG_VALUE = 10l;

  private User user;
  private Group group;
  private Client client;
  private Shop shop;
  private Form form;
  private Role role;
  private RegisterRequest request;

  @BeforeEach
  void setUp() {

    ReflectionTestUtils.setField(userService, "timeLimit", INT_VALUE);

    user = generator.nextObject(User.class);
    group = generator.nextObject(Group.class);
    client = generator.nextObject(Client.class);
    shop = generator.nextObject(Shop.class);
    form = generator.nextObject(Form.class);
    role = generator.nextObject(Role.class);
    request = generator.nextObject(RegisterRequest.class);
  }

  @Test
  void givenId_whenDelete_thenVerify() {

    group.setTotalUsers(2);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(groupService.getByUsername(anyString())).thenReturn(group);

    userService.delete(LONG_VALUE);
    verify(groupService, times(1)).update(any(Group.class));
    verify(userRepository, times(1)).delete(any(User.class));
  }

  @Test
  void givenUser_whenDelete_thenVerify() {

    group.setTotalUsers(1);
    user.setEnabled(true);
    client.setForms(List.of(form));

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(clientService.findAllByUser(any(User.class))).thenReturn(List.of(client));
    when(shopService.findAllByUser(any(User.class))).thenReturn(List.of(shop));

    userService.delete(user);
    verify(formService, times(1)).deleteAll(anySet());
    verify(clientService, times(1)).deleteAll(anyList());
    verify(shopService, times(1)).deleteAll(anyList());
    verify(userRepository, times(1)).delete(any(User.class));
    verify(groupService, times(1)).delete(any(Group.class));
  }

  @Test
  void givenTrue_whenCreate_thenReturnUser() throws Exception {

    request.setMaxGroup(null);

    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(groupService.create(anyInt(), anyString())).thenReturn(group);
    when(roleService.findByName(anyString())).thenReturn(role);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(groupService.update(any(Group.class))).thenReturn(group);

    final User response = userService.create(request, true);
    assertEquals(user.getFirstName(), response.getFirstName());
  }

  @Test
  void givenFalse_whenCreate_thenReturnUser() throws Exception {

    request.setMaxGroup(null);
    group.setMaxUsers(2);
    group.setTotalUsers(1);

    try (MockedStatic<JwtTokenUtil> tokenUtil = mockStatic(JwtTokenUtil.class)) {

      when(userRepository.existsByUsername(anyString())).thenReturn(false);
      tokenUtil.when(JwtTokenUtil::getUsername).thenReturn(STR_VALUE);
      when(groupService.getByUsername(anyString())).thenReturn(group);
      when(roleService.findByName(anyString())).thenReturn(role);
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(groupService.update(any(Group.class))).thenReturn(group);

      final User response = userService.create(request, false);
      assertEquals(user.getFirstName(), response.getFirstName());
    }
  }

  @Test
  void givenFalse_whenCreate_thenThrowException() throws Exception {

    request.setMaxGroup(null);
    group.setMaxUsers(2);
    group.setTotalUsers(2);

    try (MockedStatic<JwtTokenUtil> tokenUtil = mockStatic(JwtTokenUtil.class)) {

      when(userRepository.existsByUsername(anyString())).thenReturn(false);
      tokenUtil.when(JwtTokenUtil::getUsername).thenReturn(STR_VALUE);
      when(groupService.getByUsername(anyString())).thenReturn(group);

      final UsersLimitException thrown = assertThrows(
          UsersLimitException.class,
          () -> userService.create(request, false));
      assertEquals(Message.MAX_USERS_ERROR, thrown.getMessage());
    }
  }

  @Test
  void whenCreate_thenThrowException() {

    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    final DuplicateKeyException thrown = assertThrows(
        DuplicateKeyException.class,
        () -> userService.create(request, false));
    assertEquals(Message.DUPLICATE_USERNAME, thrown.getMessage());
  }

  @Test
  void whenGetAll_thenReturnPage() {

    List<User> users = generator.objects(User.class, 5).toList();
    Page<User> page = new PageImpl<>(users);

    when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
    final Page<User> response = userService.getAll(
        INT_VALUE, INT_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, null, null, 
        null, null, null, STR_VALUE);
    assertEquals(users.size(), response.getNumberOfElements());
    assertEquals(1, response.getTotalPages());
  }

  @Test
  void whenEnable_thenReturnUser() throws UsersLimitException {

    user.setEnabled(false);
    user.setGroup(group);
    group.setTotalUsers(1);
    group.setMaxUsers(2);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(groupService.update(any(Group.class))).thenReturn(group);
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.enable(LONG_VALUE);
    assertEquals(user.getUsername(), response.getUsername());
  }

  @Test
  void whenEnable_thenThrowException() {

    user.setEnabled(false);
    user.setGroup(group);
    group.setTotalUsers(2);
    group.setMaxUsers(2);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    final UsersLimitException thrown = assertThrows(
      UsersLimitException.class,
      () -> userService.enable(LONG_VALUE));
    assertEquals(Message.MAX_USERS_ERROR, thrown.getMessage());
  }

  @Test
  void whenDisable_thenReturnUser() {

    user.setEnabled(true);
    user.setGroup(group);
    group.setUsers(List.of(user));

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(groupService.update(any(Group.class))).thenReturn(group);
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.disable(LONG_VALUE);
    assertEquals(user.getLastName(), response.getLastName());
  }

  @Test
  void whenGeneratePasswordCode_thenReturnString() throws SparkPostException {

    PasswordUpdateRequest request = generator.nextObject(PasswordUpdateRequest.class);

    try (MockedStatic<CodeGeneration> codeGeneration = mockStatic(CodeGeneration.class)) {

      when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
      codeGeneration.when(CodeGeneration::newCode).thenReturn(INT_VALUE);
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(sparkPostService.sendMesage(anyString(), anyString(), anyString())).thenReturn(true);

      final String response = userService.generatePasswordCode(request);
      assertEquals(Message.CHECK_EMAIL, response);
    }
  }

  @Test
  void whenGenerateUsernameCode_thenReturnErrorString() throws SparkPostException {

    UsernameUpdateRequest request = generator.nextObject(UsernameUpdateRequest.class);

    try (MockedStatic<CodeGeneration> codeGeneration = mockStatic(CodeGeneration.class)) {

      when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
      codeGeneration.when(CodeGeneration::newCode).thenReturn(INT_VALUE);
      when(userRepository.save(any(User.class))).thenReturn(user);
      when(sparkPostService.sendMesage(anyString(), anyString(), anyString())).thenReturn(false);

      final String response = userService.generateUsernameCode(request);
      assertEquals(Message.SPARK_POST_ERROR, response);
    }
  }

  @Test
  void whenConfirmNewPassword_thenReturnString() throws CodeException {

    CodeConfirmationRequest request = generator.nextObject(CodeConfirmationRequest.class);
    request.setCode(INT_VALUE);
    
    user.setPasswordCode(INT_VALUE);
    user.setPasswordTimeLimit(LocalDateTime.MAX);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    final String response = userService.confirmNewPassword(request);
    assertEquals(Message.UPDATED_PASSWORD, response);
  }

  @Test
  void whenConfirmNewUsername_thenReturnString() throws CodeException {

    CodeConfirmationRequest request = generator.nextObject(CodeConfirmationRequest.class);
    request.setCode(INT_VALUE);
    
    user.setUsernameCode(INT_VALUE);
    user.setUsernameTimeLimit(LocalDateTime.MAX);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.confirmNewUsername(request);
    assertEquals(user.getFirstName(), response.getFirstName());
  }

  @Test
  void givenWrongCode_whenConfirmNewUsername_thenThrowException() throws CodeException {

    CodeConfirmationRequest request = generator.nextObject(CodeConfirmationRequest.class);
    request.setCode(INT_VALUE);
    
    user.setUsernameCode(INT_VALUE -1);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    final CodeException thrown = assertThrows(
        CodeException.class, 
        () -> userService.confirmNewUsername(request));
    assertEquals(Message.INVALID_CODE, thrown.getMessage());
  }

  @Test
  void givenOldDate_whenConfirmNewUsername_thenThrowException() throws CodeException {

    CodeConfirmationRequest request = generator.nextObject(CodeConfirmationRequest.class);
    request.setCode(INT_VALUE);
    
    user.setUsernameCode(INT_VALUE );
    user.setUsernameTimeLimit(LocalDateTime.MIN);

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    final CodeException thrown = assertThrows(
        CodeException.class, 
        () -> userService.confirmNewUsername(request));
    assertEquals(Message.CODE_EXPIRED, thrown.getMessage());
  }

  @Test
  void whenUpdateNames_thenReturnUser() {

    final NamesUpdateRequest request = generator.nextObject(NamesUpdateRequest.class);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.updateNames(request);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenUpdateLastPayment_thenReturnUser() {

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.updateLastPayment(LONG_VALUE, LocalDate.MAX);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenUpdateComments_thenReturnUser() {

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.updateComments(LONG_VALUE, STR_VALUE);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenGetIfEnabled_thenReturnUser() {

    when(userRepository.findByIdAndIsEnabledTrue(anyLong())).thenReturn(Optional.of(user));

    final User response = userService.getIfEnabled(LONG_VALUE);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenGetIfEnabled_thenThrowException() {

    when(userRepository.findByIdAndIsEnabledTrue(anyLong())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class, 
        () -> userService.getIfEnabled(LONG_VALUE));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenFindByUsername_thenReturnUser() {

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    final User response = userService.findByUsername(STR_VALUE);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenFindByUsername_thenThrowException() {

    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    final UsernameNotFoundException thrown = assertThrows(
        UsernameNotFoundException.class, 
        () -> userService.findByUsername(STR_VALUE));
    assertEquals(Message.USERNAME_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenGet_thenReturnUser() {

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    final User response = userService.get(LONG_VALUE);
    assertEquals(user.getComments(), response.getComments());
  }

  @Test
  void whenGet_thenThrowException() {

    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class, 
        () -> userService.get(LONG_VALUE));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenUpdate_thenReturnUser() {

    when(userRepository.save(any(User.class))).thenReturn(user);

    final User response = userService.update(user);
    assertEquals(user.getComments(), response.getComments());
  }

}
