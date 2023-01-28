package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.User;

public interface UserRepository extends GenericRepository<User> {
    
    Optional<User> findByUsername(String username);
    
    Boolean existsByUsername(String username);

    List<User> findByRolesIn(List<Role> roles);

    @Query(value = "SELECT s.id FROM forms.shop s INNER JOIN forms.user_shops us ON us.user_id = :id", nativeQuery = true)
    Set<Long> findShopIds(@Param("id") Long id);
}
