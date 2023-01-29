package com.danieljoanol.forms.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ClientRepository;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.repository.UserRepository;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {

    private final FormRepository formRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public FormServiceImpl(FormRepository formRepository, ClientRepository clientRepository,
            UserRepository userRepository) {
        super(formRepository);
        this.formRepository = formRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Form create(Form form, Long clientId, String username) {
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Message.USERNAME_NOT_FOUND));

        Optional<Client> client = user.getShops().stream()
                .flatMap(shop -> shop.getClients().stream())
                .filter(c -> c.getId() == clientId)
                .findAny();

        if (client.isEmpty()) {
            throw new EntityNotFoundException(Message.ENTITY_NOT_FOUND);
        }

        form.setId(null);
        form.setEnabled(true);
        client.get().getForms().add(form);
        form = formRepository.save(form);
        clientRepository.save(client.get());

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

    @Override
    public void delete(Long id) {
        Form form = get(id);
        formRepository.delete(form);
    }

    @Override
    public void deleteAllByIds(Iterable<? extends Long> ids) {
        formRepository.deleteAllById(ids);

    }

}
