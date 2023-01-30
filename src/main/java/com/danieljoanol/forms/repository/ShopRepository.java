package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopRepository extends GenericRepository<Shop> {

    Page<Shop> findByGroup_Users_UsernameInAndIsEnabledTrue(Pageable pageable, List<String> usernames);

    List<Shop> findByGroup_Users_UsernameIn(List<String> usernames);

    List<Shop> findByGroup_UsersIn(List<User> users);

    Optional<Shop> findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(Long id, List<String> usernames);
    
}
