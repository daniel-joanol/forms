package com.danieljoanol.forms.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {

    private final FormRepository formRepository;
    private final UserService userService;
    private final ClientService clientService;

    public FormServiceImpl(FormRepository formRepository, UserService userService, ClientService clientService) {
        super(formRepository);
        this.formRepository = formRepository;
        this.clientService = clientService;
        this.userService = userService;
    }

    @Override
    public Form create(Form form, Long shopId, Long clientId) throws NoParentException {
        User user = JwtTokenUtil.getUserFromContext(userService);
        Client actualClient = clientService.get(clientId);
        
        Set<Shop> shops = user.getShops();
        if (shops == null) {
            throw new NoParentException(Message.noParentEx("shop", "user"));
        }

        Shop actualShop = null;
        Set<Client> clients = null;
        for (Shop shop : shops) {
            if (shop.getId() == shopId) {
                for (Client client : shop.getClients()) {
                    if (client.getId() == clientId) {
                        actualShop = shop;
                        clients = shop.getClients();
                    }
                }
            }
        }
        
        if (clients == null || actualShop == null) {
            throw new NoParentException(Message.doesNotContain("client", "shop"));
        }

        form.setEnabled(true);
        form = formRepository.save(form);
        actualClient.getForms().add(form);
        actualClient = clientService.update(actualClient);
        return form;
    }

    @Override
    public Form updateIfEnabled(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void disable(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Form enable(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
