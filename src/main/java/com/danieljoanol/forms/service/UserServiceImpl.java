package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Email;
import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.user.CodeConfirmationRequest;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.controller.request.user.PasswordUpdateRequest;
import com.danieljoanol.forms.controller.request.user.UsernameUpdateRequest;
import com.danieljoanol.forms.email.SparkPostService;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.CodeException;
import com.danieljoanol.forms.repository.UserRepository;
import com.danieljoanol.forms.util.CodeGeneration;
import com.sparkpost.exception.SparkPostException;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    private final UserRepository userRepository;
    private final SparkPostService sparkPostService;
    private final PasswordEncoder encoder;

    @Value("${forms.app.code.limit}")
    private Integer timeLimit;

    public UserServiceImpl(UserRepository userRepository, SparkPostService sparkPostService,
            PasswordEncoder encoder) {
        super(userRepository);
        this.userRepository = userRepository;
        this.sparkPostService = sparkPostService;
        this.encoder = encoder;
    }

    @Override
    public User create(User user) {
        user.setId(null);
        user.setEnabled(true);
        return userRepository.save(user);
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
    public User updateNames(NamesUpdateRequest request) {
        User entity = get(request.getId());
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        return update(entity);
    }

    @Override
    public User updateComments(Long id, String comments) {
        User entity = get(id);
        entity.setComments(comments);
        return update(entity);
    }

    @Override
    public User updateLastPayment(Long id, LocalDate date) {
        User entity = get(id);
        entity.setLastPayment(date);
        return update(entity);
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
    public String confirmNewPassword(CodeConfirmationRequest request) throws CodeException {
        User user = findByUsername(request.getUsername());

        if (user.getPasswordCode() == null || user.getPasswordCode() != request.getCode()) {
            throw new CodeException(Message.INVALID_CODE);
        }

        if (LocalDateTime.now().isAfter(user.getPasswordTimeLimit())) {
            throw new CodeException(Message.CODE_EXPIRED);
        }

        user.setPassword(user.getNewPassword());
        user = update(user);

        return Message.UPDATED_PASSWORD;
    }

    @Override
    public User confirmNewUsername(CodeConfirmationRequest request) throws CodeException {
        User user = findByUsername(request.getUsername());

        if (user.getUsernameCode() == null || user.getUsernameCode() != request.getCode()) {
            throw new CodeException(Message.INVALID_CODE);
        }

        if (LocalDateTime.now().isAfter(user.getUsernameTimeLimit())) {
            throw new CodeException(Message.CODE_EXPIRED);
        }

        user.setUsername(user.getNewUsername());
        user = update(user);

        return user;
    }
    
}
