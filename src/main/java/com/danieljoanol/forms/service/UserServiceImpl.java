package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Email;
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
import com.danieljoanol.forms.repository.criteria.UserCriteria;
import com.danieljoanol.forms.repository.specification.UserSpecification;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.util.CodeGeneration;
import com.sparkpost.exception.SparkPostException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final SparkPostService sparkPostService;
  private final PasswordEncoder encoder;
  private final RoleService roleService;

  private final ShopService shopService;
  private final ClientService clientService;
  private final FormService formService;
  private final GroupService groupService;

  @Value("${forms.app.code.limit}")
  private Integer timeLimit;

  @Override
  public void delete(Long id) {
    User user = get(id);
    delete(user);
  }

  @Override
  public void delete(User user) {

    Group group = groupService.getByUser(user);
    if (user.isEnabled()) {
      group.setTotalUsers(group.getTotalUsers() - 1);
    }

    // Makes a double validation
    if (group.getUsers().size() <= 1 && group.getTotalUsers() == 0) {

      List<Client> clients = clientService.findAllByUser(user);
      Set<Form> forms = clients.stream()
          .flatMap(client -> client.getForms().stream())
          .collect(Collectors.toSet());
      clients.stream().forEach(c -> c.setForms(null));
      formService.deleteAll(forms);

      clientService.deleteAll(clients);

      List<Shop> shops = shopService.findAllByUser(user);
      shopService.deleteAll(shops);

      group.setUsers(null);
      userRepository.delete(user);
      groupService.delete(group);

    } else {
      group.getUsers().remove(user);
      group = groupService.update(group);
      userRepository.delete(user);
    }
  }

  @Override
  public User create(RegisterRequest request, boolean firstUser) throws Exception {

    Group group = null;

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new DuplicateKeyException(Message.DUPLICATE_USERNAME);
    }

    if (firstUser) {

      if (request.getMaxGroup() == null) {
        request.setMaxGroup(1);
      }
      group = groupService.create(
          request.getMaxGroup(),
          request.getUsername().substring(0, 5));

    } else {

      String username = JwtTokenUtil.getUsername();
      group = groupService.getByUsername(username);

      if (group.getMaxUsers() == group.getTotalUsers()) {
        throw new UsersLimitException(Message.MAX_USERS_ERROR);
      } else {
        group.setTotalUsers(group.getTotalUsers() + 1);
      }
    }

    Role userRole = roleService.findByName("ROLE_USER");

    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .username(request.getUsername())
        .password(encoder.encode(request.getPassword()))
        .roles(Set.of(userRole))
        .group(group)
        .isEnabled(true)
        .build();

    user = userRepository.save(user);

    group.getUsers().add(user);
    group = groupService.update(group);

    return user;

  }

  @Override
  public Page<User> getAll(Integer pageNumber, Integer pageSize, String firstName, String lastName, String username,
      LocalDate minLastPayment, LocalDate maxLastPayment, Boolean isEnabled, LocalDate minDisabledDate,
      LocalDate maxDisabledDate,
      String groupName) {

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    UserCriteria criteria = UserCriteria.builder()
        .firstName(firstName)
        .lastName(lastName)
        .username(username)
        .minLastPayment(minLastPayment)
        .maxLastPayment(maxLastPayment)
        .isEnabled(isEnabled)
        .minDisabledDate(minDisabledDate)
        .maxDisabledDate(maxDisabledDate)
        .groupName(groupName)
        .build();

    return userRepository.findAll(UserSpecification.search(criteria), pageable);
  }

  @Override
  public User enable(Long id) throws UsersLimitException {

    User user = get(id);
    if (user.isEnabled())
      return user;

    Group group = user.getGroup();
    if (group.getTotalUsers() < group.getMaxUsers()) {
      group.setTotalUsers(group.getTotalUsers() + 1);
      group = groupService.update(group);
    } else {
      throw new UsersLimitException(Message.MAX_USERS_ERROR);
    }

    user.setEnabled(true);
    user.setDisabledDate(null);
    return update(user);
  }

  @Override
  public User disable(Long id) {

    User user = get(id);
    if (!user.isEnabled())
      return user;

    Group group = groupService.getByUser(user);
    group.setTotalUsers(group.getTotalUsers() - 1);

    List<User> tempUsers = new ArrayList<>();
    for (User actualUser : group.getUsers()) {
      if (actualUser.getId() != id) {
        tempUsers.add(actualUser);
      }
    }

    group.setUsers(tempUsers);
    group = groupService.update(group);

    user.setEnabled(false);
    user.setDisabledDate(LocalDate.now());
    return update(user);
  }

  @Override
  public String generatePasswordCode(PasswordUpdateRequest request) throws SparkPostException {

    User user = findByUsername(request.getUsername());
    user.setNewPassword(encoder.encode(request.getNewPassword()));
    user.setPasswordCode(CodeGeneration.newCode());
    user.setPasswordTimeLimit(LocalDateTime.now().plusMinutes(timeLimit));

    user = update(user);
    return sendEmail(user.getUsername(), user.getFirstName(), user.getPasswordCode());
  }

  @Override
  public String generateUsernameCode(UsernameUpdateRequest request) throws SparkPostException {

    User user = findByUsername(request.getActualUsername());
    user.setNewUsername(request.getNewUsername());
    user.setUsernameCode(CodeGeneration.newCode());
    user.setUsernameTimeLimit(LocalDateTime.now().plusMinutes(timeLimit));

    user = update(user);
    return sendEmail(user.getNewUsername(), user.getFirstName(), user.getUsernameCode());
  }

  @Override
  public String confirmNewPassword(CodeConfirmationRequest request) throws CodeException {

    User user = findByUsername(request.getUsername());
    validateCode(request, user.getPasswordCode(), user.getPasswordTimeLimit());

    user.setPassword(user.getNewPassword());
    user = update(user);

    return Message.UPDATED_PASSWORD;
  }

  @Override
  public User confirmNewUsername(CodeConfirmationRequest request) throws CodeException {

    User user = findByUsername(request.getUsername());
    validateCode(request, user.getUsernameCode(), user.getUsernameTimeLimit());

    user.setUsername(user.getNewUsername());
    user = update(user);

    return user;
  }

  @Override
  public User updateNames(NamesUpdateRequest request) {

    User entity = get(request.getId());
    entity.setFirstName(request.getFirstName());
    entity.setLastName(request.getLastName());
    return update(entity);
  }

  @Override
  public User updateLastPayment(Long id, LocalDate date) {

    User entity = get(id);
    entity.setLastPayment(date);
    return update(entity);
  }

  @Override
  public User updateComments(Long id, String comments) {

    User entity = get(id);
    entity.setComments(comments);
    return update(entity);
  }

  @Override
  public User getIfEnabled(Long id) {
    return userRepository.findByIdAndIsEnabledTrue(id)
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(Message.USERNAME_NOT_FOUND));
  }

  private void validateCode(CodeConfirmationRequest request, Integer code, LocalDateTime date) throws CodeException {

    if (code == null || code != request.getCode()) {
      throw new CodeException(Message.INVALID_CODE);
    }

    if (LocalDateTime.now().isAfter(date)) {
      throw new CodeException(Message.CODE_EXPIRED);
    }
  }

  private String sendEmail(String username, String firstName, Integer code) throws SparkPostException {

    Boolean isEmailSent = sparkPostService.sendMesage(
        username,
        Email.CODE_TITLE,
        Email.codeMessage(firstName, code, timeLimit));

    if (isEmailSent) {
      return Message.CHECK_EMAIL;
    } else {
      return Message.SPARK_POST_ERROR;
    }
  }

  @Override
  public User get(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public User update(User update) {
    return userRepository.save(update);
  }

  @Override
  public void deleteUsersByGroup(Long groupId) {
    Group group = groupService.get(groupId);
    group.getUsers().forEach(u -> delete(u));
    
    //Removes deactivated users
    List<User> users = findByGroup(group);
    users.forEach(u -> delete(u));
  }

  @Override
  public Group disableUserByGroup(Long groupId) {
    Group group = groupService.get(groupId);
    group.getUsers().forEach(u -> {
      u.setEnabled(false);
      u.setDisabledDate(LocalDate.now());
      u = update(u);
    });

    return group;
  }

  //TODO: check if it's still used
  //TODO: create a scheduled method to clean the db
  @Override
  public List<User> findDisabledUsers(LocalDate date) {
    return userRepository.findByIsEnabledFalseAndDisabledDateLessThan(date);
  }

  @Override
  public void deleteUsers(List<User> users) {
    userRepository.deleteAll(users);
  }

  @Override
  public List<User> findByGroup(Group group) {
    return userRepository.findByGroup(group);
  }

}
