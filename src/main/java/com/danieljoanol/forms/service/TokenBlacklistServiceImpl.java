package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.TokenBlacklist;
import com.danieljoanol.forms.repository.TokenBlacklistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    private final TokenBlacklistRepository blacklistRepository;

    @Override
    public TokenBlacklist save(String token) {
        TokenBlacklist blacklisted = new TokenBlacklist();
        blacklisted.setToken(token);
        blacklisted.setDate(LocalDateTime.now());
        return blacklistRepository.save(blacklisted);
    }
    
    @Override
    public void deleteByDate(LocalDateTime date) {
        blacklistRepository.deleteByDateBefore(date);
        
    }

    @Override
    public boolean existsByToken(String token) {
        return blacklistRepository.existsByToken(token);
    }

}
