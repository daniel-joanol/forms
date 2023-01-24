package com.danieljoanol.forms.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.ClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {
    
    private final ClientRepository clientRepository;
    private final UserService userService;
    private final ShopService shopService;

    public ClientServiceImpl(ClientRepository clientRepository, UserService userService, ShopService shopService) {
        super(clientRepository);
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.shopService = shopService;
    }

    @Override
    public Client create(Client client) throws NoParentException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        Shop shop = user.getShop();
        if (shop == null) {
            throw new NoParentException(Message.nullPointerEx("shop", "user"));
        }
        client = clientRepository.save(client);
        shop = shopService.update(shop);
        return client;
    }

}
