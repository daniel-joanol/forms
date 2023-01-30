package com.danieljoanol.forms.repository;

import java.util.List;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopRepository extends GenericRepository<Shop> {
    
    List<Shop> findByGroup_Users_UsernameIn(List<String> usernames);

    List<Shop> findByGroup_UsersIn(List<User> users);

}
