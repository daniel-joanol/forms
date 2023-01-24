package com.danieljoanol.forms.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danieljoanol.forms.entity.TokenBlacklist;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    
    Boolean existsByToken(String token);
    Long deleteByDateBefore(LocalDateTime date);
    
}
