package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

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
    @Transactional
    public Long deleteByDate(LocalDateTime date) {
        return blacklistRepository.deleteByDateBefore(date);
        
    }

    @Override
    public Boolean existsByToken(String token) {
        return blacklistRepository.existsByToken(token);
    }

}
