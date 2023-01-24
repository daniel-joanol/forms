package com.danieljoanol.forms.service;

import java.time.LocalDate;

import com.danieljoanol.forms.controller.request.user.CodeConfirmationRequest;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.controller.request.user.PasswordUpdateRequest;
import com.danieljoanol.forms.controller.request.user.UsernameUpdateRequest;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.CodeException;
import com.sparkpost.exception.SparkPostException;

public interface UserService extends GenericService<User> {
    
    User create(User user);
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    User updateNames(NamesUpdateRequest request);
    User updateLastPayment(Long id, LocalDate date);
    User updateComments(Long id, String comments);
    String generatePasswordCode(PasswordUpdateRequest request) throws SparkPostException;
    String generateUsernameCode(UsernameUpdateRequest request) throws SparkPostException;
    String confirmNewPassword(CodeConfirmationRequest request) throws CodeException;
    User confirmNewUsername(CodeConfirmationRequest request) throws CodeException;

}
