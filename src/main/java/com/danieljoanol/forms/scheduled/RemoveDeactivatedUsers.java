package com.danieljoanol.forms.scheduled;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class RemoveDeactivatedUsers {
  
  @Value("${forms.app.cleanafter.months}")
  private Integer timeLimit;

  private final UserService userService;

  @Scheduled(cron = "0 0 2 10 * ?", zone = "Europe/Madrid")
  public void startProcess() {
    
    log.info("Removing deactivated users");

    List<User> users = userService.findDisabledUsers(LocalDate.now().minusMonths(timeLimit));
    users.forEach(
      u -> userService.delete(u)
    );

    log.info("Finished removing deactivated users");
  }
}
