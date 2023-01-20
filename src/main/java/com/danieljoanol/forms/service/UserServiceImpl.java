package com.danieljoanol.forms.service;

import java.time.LocalDate;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.UserRepository;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
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

    //TODO: Create UserController
    
}
