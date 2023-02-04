package com.danieljoanol.forms.scheduled;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.service.ClientService;
import com.danieljoanol.forms.service.FormService;
import com.danieljoanol.forms.service.GroupService;
import com.danieljoanol.forms.service.ShopService;
import com.danieljoanol.forms.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@Service
@RequiredArgsConstructor
public class CleanDatabase {
  
  @Value("${forms.app.cleanafter.months}")
  private Integer maxMonths;

  private final UserService userService;
  private final GroupService groupService;
  private final ShopService shopService;
  private final ClientService clientService;
  private final FormService formService;

  //TODO: add scheduled and test
  public void startProcess() {

    LocalDate date = LocalDate.now().minusMonths(maxMonths);

    log.info("Cleaning database");
    cleanForms(date);
    cleanClients(date);
    cleanShops(date);
    cleanUsersAndGroups(date);
    log.info("Finished cleaning database");
  }

  private void cleanForms(LocalDate date) {
    Long forms = formService.cleanDatabase(date);
    log.info("Total forms removed: {}", forms);
  }

  private void cleanClients(LocalDate date) {
    Long clients = clientService.cleanDatabase(date);
    log.info("Total clients removed: {}", clients);
  }

  private void cleanShops(LocalDate date) {
    Long shops = shopService.cleanDatabase(date);
    log.info("Total shops removed: {}", shops);
  }

  private void cleanUsersAndGroups(LocalDate date) {
    
    List<User> disabledUsers = userService.findDisabledUsers(date);
    Map<Group, List<User>> mapUsersByGroup = disabledUsers
        .stream()
        .collect(Collectors.groupingBy(User::getGroup));

    List<Group> groupsToRemove = new ArrayList<>();
    List<User> usersToRemove = new ArrayList<>();

    mapUsersByGroup.keySet().forEach(
      (group) -> {
        List<User> users = mapUsersByGroup.get(group);
        if (group.getMaxUsers() == users.size()) {
          groupsToRemove.add(group);
          usersToRemove.addAll(users);
        }
      }
    );

    groupService.deleteGroups(groupsToRemove);
    log.info("Total groups removed: {}", groupsToRemove.size());
    userService.deleteUsers(usersToRemove);
    log.info("Total users removed: {}", usersToRemove.size());

  }

}
