package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.User;

public interface ClientService extends GenericService<Client> {

  public Client create(Client client, String username);

  public Client updateIfEnabled(Client client);

  public Client enable(Long id);

  public void deleteAllByIds(Iterable<? extends Long> ids);

  public Client findByIdAndUsernames(Long id, List<String> usernames);

  public Page<Client> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String name, String city, String province, String phone, String email, String document);

  public List<Client> findAllByUsername(String username);

  public List<Client> findAllByUser(User user);

  public Client getIfEnabled(Long id, String username);
}
