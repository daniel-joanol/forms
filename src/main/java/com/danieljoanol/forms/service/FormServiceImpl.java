package com.danieljoanol.forms.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.repository.FormRepository;
import com.danieljoanol.forms.repository.criteria.FormCriteria;
import com.danieljoanol.forms.repository.specification.FormSpecification;

@Service
public class FormServiceImpl extends GenericServiceImpl<Form> implements FormService {

  private final FormRepository formRepository;
  private final ClientService clientService;
  private final GroupService groupService;

  public FormServiceImpl(
      FormRepository formRepository, ClientService clientService, GroupService groupService) {
    super(formRepository);
    this.formRepository = formRepository;
    this.clientService = clientService;
    this.groupService = groupService;
  }

  @Override
  public Form create(Form form, Long clientId, String username) {

    Client client = clientService.findByIdAndUsernames(clientId, List.of(username));

    form.setId(null);
    form.setGroup(client.getGroup());
    client.getForms().add(form);
    form = formRepository.save(form);
    clientService.update(client);

    return form;
  }

  @Override
  public Page<Form> findByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String plate, String model, String brand, String frame, String agent, LocalDateTime minDate,
      LocalDateTime maxDate, Boolean openOrder) {

    Group group = groupService.getByUsername(username);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    FormCriteria criteria = FormCriteria.builder()
        .plate(plate)
        .model(model)
        .brand(brand)
        .frame(frame)
        .agent(agent)
        .minDate(minDate)
        .maxDate(maxDate)
        .openOrder(openOrder)
        .group(group)
        .build();

    return formRepository.findAll(FormSpecification.search(criteria), pageable);
  }

  @Override
  public Form get(Long id, String username) {
    return formRepository.findByIdAndGroup_Users_UsernameIn(id, List.of(username))
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public void delete(Long id, String username) {
    Form form = get(id, username);
    formRepository.delete(form);
  }

  @Override
  public void deleteAll(Iterable<? extends Form> forms) {
    formRepository.deleteAll(forms);
  }

  @Override
  public Form closeOrOpenOrder(Long id, String username, Boolean state) {
    Form form = get(id, username);
    form.setOpenOrder(state);
    return formRepository.save(form);
  }

  @Override
  public Form update(Form update, String username) {
    Form form = get(update.getId(), username);
    return formRepository.save(form);
  }

}
