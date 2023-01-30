package com.danieljoanol.forms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.repository.FormRepository;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {

    private final FormRepository formRepository;
    private final ClientService clientService;
    private final GroupService groupService;

    public FormServiceImpl(FormRepository formRepository, ClientService clientService, 
            GroupService groupService) {
        super(formRepository);
        this.formRepository = formRepository;
        this.clientService = clientService;
        this.groupService = groupService;
    }

    @Override
    public Form create(Form form, Long clientId, String username) {

        Group group = groupService.getByUsernameIn(List.of(username));
        Optional<Client> client = group.getClients().stream()
                .filter(c -> c.getId() == clientId)
                .findAny();

        if (client.isEmpty()) {
            throw new AccessDeniedException(Message.NOT_AUTHORIZED);
        }

        form.setId(null);
        form.setEnabled(true);
        client.get().getForms().add(form);
        form = formRepository.save(form);
        clientService.update(client.get());

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
