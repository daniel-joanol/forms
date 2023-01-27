package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.util.List;

import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.controller.request.user.CodeConfirmationRequest;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.controller.request.user.PasswordUpdateRequest;
import com.danieljoanol.forms.controller.request.user.UsernameUpdateRequest;
import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.CodeException;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.sparkpost.exception.SparkPostException;

public interface UserService extends GenericService<User> {
    
    public User create(RegisterRequest request, boolean mainUser) throws Exception;

    public User findByUsername(String username);

    public Boolean existsByUsername(String username);

    public User updateNames(NamesUpdateRequest request);

    public User updateLastPayment(Long id, LocalDate date);

    public User updateComments(Long id, String comments);

    public String generatePasswordCode(PasswordUpdateRequest request) throws SparkPostException;

    public String generateUsernameCode(UsernameUpdateRequest request) throws SparkPostException;

    public String confirmNewPassword(CodeConfirmationRequest request) throws CodeException;

    public User confirmNewUsername(CodeConfirmationRequest request) throws CodeException;

    public List<User> getUsersByRole(List<Role> roles);

    public User enable(Long id) throws UsersLimitException;

}
