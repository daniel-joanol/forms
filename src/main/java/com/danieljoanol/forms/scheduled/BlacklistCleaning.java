package com.danieljoanol.forms.scheduled;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.service.TokenBlacklistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class BlacklistCleaning {

    @Value("${forms.app.jwtExpirationMs}")
    private Integer expirationMs;

    private final TokenBlacklistService blacklistService;

    @Scheduled(cron = "0 0 2 15 * ?", zone = "Europe/Madrid")
    public void main() {

        log.info("Removing old tokens from blacklist");
        Long total = blacklistService.deleteByDate(
                LocalDateTime.now().minusMinutes(
                    (expirationMs / 1000 / 60) - 1));
        log.info("Total tokens removed: {}", total);
    }

}
