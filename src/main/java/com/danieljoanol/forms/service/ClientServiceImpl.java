package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Group;
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
        client = clientRepository.save(client);

        group.getClients().add(client);
        group = groupService.update(group);

        return client;
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
