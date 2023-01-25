package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import com.danieljoanol.forms.entity.TokenBlacklist;

public interface TokenBlacklistService {
    
    Boolean existsByToken(String token);
    TokenBlacklist save(String token);
    Long deleteByDate(LocalDateTime date);

}
