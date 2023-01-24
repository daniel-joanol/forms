package com.danieljoanol.forms.service;

import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.exception.NoParentException;
import com.danieljoanol.forms.repository.FormRepository;

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
    public Form create(Form form, Long clientId) throws NoParentException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        Client actualClient = clientService.get(clientId);
        
        Shop shop = user.getShop();
        if (shop == null) {
            throw new NoParentException(Message.nullPointerEx("shop", "user"));
        }
        
        Set<Client> clients = shop.getClients();
        if (!clients.contains(actualClient)) {
            throw new NoParentException(Message.doesNotContain("client", "shop"));
        }

        form = formRepository.save(form);
        actualClient.getForms().add(form);
        actualClient = clientService.update(actualClient);
        return form;
    }

}
