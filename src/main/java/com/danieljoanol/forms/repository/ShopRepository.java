package com.danieljoanol.forms.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopRepository extends GenericRepository<Shop> {

    List<Shop> findByGroup_Users_UsernameIn(List<String> usernames);

    List<Shop> findByGroup_UsersIn(List<User> users);

    Optional<Shop> findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(Long id, List<String> usernames);
    
    Long deleteByIsEnabledFalseAndDisabledDateLessThan(LocalDate date);
    
}
