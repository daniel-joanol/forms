package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.repository.ShopRepository;
import com.danieljoanol.forms.repository.UserRepository;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

    private final ClientRepository clientRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Value("${forms.app.group}")
    private String GROUP_PREFIX;

    public ClientServiceImpl(ClientRepository clientRepository, ShopRepository shopRepository,
            UserRepository userRepository) {
        super(clientRepository);
        this.clientRepository = clientRepository;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Client create(Client client, String username) {
        
        String groupName = JwtTokenUtil.getGroupRole(GROUP_PREFIX);
        List<User> groupUsers = userRepository.findByRoles_NameIn(List.of(groupName));
        List<Shop> shops = groupUsers.get(0).getShops();

        client.setEnabled(true);
        client = clientRepository.save(client);

        for (Shop shop : shops) {
            shop.getClients().add(client);
        }

        shopRepository.saveAll(shops);
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
