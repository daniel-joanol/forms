package com.danieljoanol.forms.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.repository.criteria.ClientCriteria;
import com.danieljoanol.forms.repository.specification.ClientSpecification;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

  private final ClientRepository clientRepository;
  private final GroupService groupService;

  public ClientServiceImpl(ClientRepository clientRepository, GroupService groupService) {
    super(clientRepository);
    this.clientRepository = clientRepository;
    this.groupService = groupService;
  }

  @Override
  public Client create(Client client, String username) {

    Group group = groupService.getByUsername(username);

    client.setEnabled(true);
    client.setGroup(group);
    client = clientRepository.save(client);

    return client;
  }

  @Override
  public Page<Client> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String name, String city, String province, String phone, String email, String document) {

    Group group = groupService.getByUsername(username);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    ClientCriteria criteria = ClientCriteria.builder()
        .name(name)
        .city(city)
        .province(province)
        .phone(phone)
        .email(email)
        .document(document)
        .group(group)
        .isEnabled(true)
        .build();

    return clientRepository.findAll(ClientSpecification.search(criteria), pageable);
  }

  @Override
  public Client findByIdAndUsernames(Long id, List<String> usernames) {
    return clientRepository.findByIdAndGroup_Users_UsernameIn(id, usernames)
        .orElseThrow(() -> new EntityNotFoundException(Message.NOT_AUTHORIZED));
  }

  @Override
  public List<Client> findAllByUser(User user) {
    return clientRepository.findByGroup_UsersIn(List.of(user));
  }

  @Override
  public List<Client> findAllByUsername(String username) {
    return clientRepository.findByGroup_Users_UsernameIn(List.of(username));
  }

  @Override
  public Client getIfEnabled(Long id, String username) {
    return clientRepository.findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(id, List.of(username))
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public Client updateIfEnabled(Client client) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void disable(Long id) {
    // TODO Auto-generated method stub

  }

  @Override
  public Client enable(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Long id) {
    Client client = get(id);
    clientRepository.delete(client);
  }

  @Override
  public void deleteAllByIds(Iterable<? extends Long> ids) {
    clientRepository.deleteAllById(ids);
  }

}
