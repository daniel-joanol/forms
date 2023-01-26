package com.danieljoanol.forms.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {
    
    private final ClientRepository clientRepository;
    private final ShopService shopService;
    private final UserService userService;

    public ClientServiceImpl(ClientRepository clientRepository, ShopService shopService, UserService userService) {
        super(clientRepository);
        this.clientRepository = clientRepository;
        this.shopService = shopService;
        this.userService = userService;
    }

    @Override
    public Client create(Client client, Long shopId) throws NoParentException {
        User user = JwtTokenUtil.getUserFromContext(userService);
        Shop actualShop = shopService.get(shopId);
        Set<Shop> shops = user.getShops();
        
        if (shops == null || !shops.contains(actualShop)) {
            throw new NoParentException(Message.noParentEx("shop", "user"));
        }
        
        client.setEnabled(true);
        client = clientRepository.save(client);
        actualShop.getClients().add(client);
        actualShop = shopService.update(actualShop);
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

}
