package com.danieljoanol.forms.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ClientRepository;

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

        Group group = groupService.getByUsernameIn(List.of(username));

        client.setEnabled(true);
        client.setGroup(group);
        client = clientRepository.save(client);

        return client;
    }

    @Override
    public List<Client> findAllByUsers(List<User> users) {
        return clientRepository.findByGroup_UsersIn(users);
    }

    @Override
    public Client findByIdAndUsernames(Long id, List<String> usernames) {
        return clientRepository.findByIdAndGroup_Users_UsernameIn(id, usernames)
                .orElseThrow(() -> new EntityNotFoundException(Message.NOT_AUTHORIZED));
    }

    @Override
    public List<Client> findAllByUsernames(List<String> usernames) {
        return clientRepository.findByGroup_Users_UsernameIn(usernames);
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
