package com.danieljoanol.forms.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.repository.FormRepository;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {

    private final FormRepository formRepository;
    private final ClientService clientService;

    public FormServiceImpl(FormRepository formRepository, ClientService clientService) {
        super(formRepository);
        this.formRepository = formRepository;
        this.clientService = clientService;
    }

    @Override
    public Form create(Form form, Long clientId, String username) {

        Client client = clientService.findByIdAndUsernames(clientId, List.of(username));

        form.setId(null);
        form.setEnabled(true);
        form.setGroup(client.getGroup());
        client.getForms().add(form);
        form = formRepository.save(form);
        clientService.update(client);

        return form;
    }

    @Override
    public Page<Form> findAllEnabledByUsername(Integer pageNumber, Integer pageSize, String username) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return formRepository.findByGroup_Users_UsernameInAndIsEnabledTrue(pageable, List.of(username));
    }

    @Override
    public Form getIfEnabled(Long id, String username) {
        return formRepository.findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(id, List.of(username))
                .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
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
