package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import com.danieljoanol.forms.entity.TokenBlacklist;

public interface TokenBlacklistService {
    
    boolean existsByToken(String token);
    TokenBlacklist save(String token);
    void deleteByDate(LocalDateTime date);

}
