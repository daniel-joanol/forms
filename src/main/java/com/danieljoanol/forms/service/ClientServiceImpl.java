package com.danieljoanol.forms.service;

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
    public Client create(Client client) throws NoParentException {
        User user = JwtTokenUtil.getUserFromContext(userService);
        Shop shop = user.getShop();
        
        if (shop == null) {
            throw new NoParentException(Message.nullPointerEx("shop", "user"));
        }
        
        client.setEnabled(true);
        client = clientRepository.save(client);
        shop.getClients().add(client);
        shop = shopService.update(shop);
        return client;
    }

}
