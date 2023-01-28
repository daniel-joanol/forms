package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Value("${forms.app.code.limit}")
    private Integer timeLimit;

    @Value("${forms.app.group}")
    private String GROUP_PREFIX;

    public UserServiceImpl(UserRepository userRepository, SparkPostService sparkPostService,
            PasswordEncoder encoder, RoleService roleService, ShopService shopService,
            ClientService clientService, FormService formService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.sparkPostService = sparkPostService;
        this.encoder = encoder;
        this.roleService = roleService;
        this.shopService = shopService;
        this.clientService = clientService;
        this.formService = formService;
    }

    @Override
    public void delete(Long id) {

        User user = get(id);
        Role groupRole = null;
        for (Role role : user.getRoles()) {
            if (role.getName().startsWith(GROUP_PREFIX)) {
                groupRole = role;
                if (user.isEnabled()) {
                    groupRole.setTotalUsers(groupRole.getTotalUsers() - 1);
                    groupRole = roleService.update(groupRole);
                }
                break;
            }
        }

        // Makes a double validation
        List<User> groupUsers = getUsersByRole(List.of(groupRole));
        if (groupUsers.size() == 1 && groupRole.getTotalUsers() == 0) {

            List<Shop> shops = user.getShops();
            if (!shops.isEmpty()) {

                List<Client> clients = shops.get(0).getClients();
                Set<Long> formIds = clients.stream()
                        .flatMap(client -> client.getForms().stream())
                        .map(Form::getId)
                        .collect(Collectors.toSet());
                clients.stream().forEach(c -> c.setForms(null));
                formService.deleteAllByIds(formIds);

                Set<Long> clientIds = clients.stream().map(Client::getId).collect(Collectors.toSet());
                shops.stream().forEach(s -> s.setClients(null));
                clientService.deleteAllByIds(clientIds);

                Set<Long> shopIds = shops.stream().map(Shop::getId).collect(Collectors.toSet());
                user.setShops(null);
                user = update(user);
                shopService.deleteAllByIds(shopIds);
            }

            userRepository.delete(user);
            roleService.delete(groupRole);

        } else {
            userRepository.delete(user);
        }

    }

    @Override
    public User create(RegisterRequest request, boolean firstUser) throws Exception {

        List<Shop> shops = new ArrayList<>();
        Role groupRole = null;

        if (existsByUsername(request.getUsername())) {
            throw new DuplicateKeyException(Message.DUPLICATE_USERNAME);
        }

        if (firstUser) {
            
            if (request.getMaxGroup() == null) {
                request.setMaxGroup(1);
            }
            groupRole = roleService.createGroupRole(request.getMaxGroup());

        } else {

            User mainUser = JwtTokenUtil.getUserFromContext(this);
            for (Role role : mainUser.getRoles()) {
                if (role.getName().startsWith(GROUP_PREFIX)) {
                    groupRole = role;
                    break;
                }
            }

            if (groupRole.getMaxUsers() == groupRole.getTotalUsers()) {
                throw new UsersLimitException(Message.MAX_USERS_ERROR);
            } else {
                groupRole.setTotalUsers(groupRole.getTotalUsers() + 1);
                shops = new ArrayList<>(mainUser.getShops());
            }
        }

        Role userRole = roleService.findByName("ROLE_USER");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .roles(Set.of(userRole, groupRole))
                .shops(shops)
                .isEnabled(true)
                .build();

        return userRepository.save(user);
        // FIXME: duplicate key value violates unique constraint (until we pass the ids
        // created in import.sql)
    }

    @Override
    public User enable(Long id) throws UsersLimitException {

        User user = get(id);

        if (user.isEnabled())
            return user;

        for (Role role : user.getRoles()) {
            if (role.getName().startsWith(GROUP_PREFIX)) {
                if (role.getTotalUsers() < role.getMaxUsers()) {
                    role.setTotalUsers(role.getTotalUsers() + 1);
                    role = roleService.update(role);
                    break;
                } else {
                    throw new UsersLimitException(Message.MAX_USERS_ERROR);
                }
            }
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

        for (Role role : user.getRoles()) {
            if (role.getName().startsWith(GROUP_PREFIX)) {
                role.setTotalUsers(role.getTotalUsers() - 1);
                role = roleService.update(role);
                break;
            }
        }

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
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Message.USERNAME_NOT_FOUND));
    }

    @Override
    public List<User> getUsersByRole(List<Role> roles) {
        return userRepository.findByRolesIn(roles);
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
