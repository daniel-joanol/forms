package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
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
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.util.CodeGeneration;
import com.sparkpost.exception.SparkPostException;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

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

    public UserServiceImpl(UserRepository userRepository, SparkPostService sparkPostService,
            PasswordEncoder encoder, RoleService roleService, ShopService shopService,
            ClientService clientService, FormService formService, GroupService groupService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.sparkPostService = sparkPostService;
        this.encoder = encoder;
        this.roleService = roleService;
        this.shopService = shopService;
        this.clientService = clientService;
        this.formService = formService;
        this.groupService = groupService;
    }

    @Override
    public void delete(Long id) {

        User user = get(id);
        Group group = groupService.getByUserIn(user);
        if (user.isEnabled()) {
            group.setTotalUsers(group.getTotalUsers() - 1);
        }

        // Makes a double validation
        if (group.getUsers().size() == 1 && group.getTotalUsers() == 0) {

            List<Client> clients = clientService.findAllByUser(user);
            Set<Long> formIds = clients.stream()
                    .flatMap(client -> client.getForms().stream())
                    .map(Form::getId)
                    .collect(Collectors.toSet());
            clients.stream().forEach(c -> c.setForms(null));
            formService.deleteAllByIds(formIds);

            Set<Long> clientIds = clients.stream().map(Client::getId).collect(Collectors.toSet());
            clientService.deleteAllByIds(clientIds);

            List<Shop> shops = shopService.findAllByUser(user);
            Set<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toSet());
            shopService.deleteAllByIds(shopIds);

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
            group = groupService.create(request.getMaxGroup());

        } else {

            String username = JwtTokenUtil.getUsername();
            group = groupService.getByUsernameIn(List.of(username));

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
                .isEnabled(true)
                .build();

        user = userRepository.save(user);

        group.getUsers().add(user);
        group = groupService.update(group);

        return user;

    }

    @Override
    public User enable(Long id) throws UsersLimitException {

        User user = get(id);
        if (user.isEnabled())
            return user;

        Group group = groupService.getByUsernameIn(List.of(user.getUsername()));
        if (group.getTotalUsers() < group.getMaxUsers()) {
            group.setTotalUsers(group.getTotalUsers() +1);
            group = groupService.update(group);
        } else {
            throw new UsersLimitException(Message.MAX_USERS_ERROR);
        }

        user.setEnabled(true);
        user.setDisabledDate(null);
        return update(user);
    }

    @Override
    public void disable(Long id) {

        User user = get(id);
        if (!user.isEnabled())
            return;

        Group group = groupService.getByUsernameIn(List.of(user.getUsername()));
        group.setTotalUsers(group.getTotalUsers() -1);
        group = groupService.update(group);

        user.setEnabled(false);
        user.setDisabledDate(new Date());
        update(user);
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

}
