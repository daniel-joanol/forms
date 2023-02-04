package com.danieljoanol.forms.service;

import java.time.LocalDate;
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
    form.setEnabled(true);
    form.setGroup(client.getGroup());
    client.getForms().add(form);
    form = formRepository.save(form);
    clientService.update(client);

    return form;
  }

  @Override
  public Page<Form> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
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
        .isEnabled(true)
        .build();

    return formRepository.findAll(FormSpecification.search(criteria), pageable);
  }

  @Override
  public Form getIfEnabled(Long id, String username) {
    return formRepository.findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(id, List.of(username))
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public void disable(Long id, String username) {
    Form form = getIfEnabled(id, username);
    form.setEnabled(false);
    form.setDisabledDate(LocalDate.now());
    update(form);
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

  @Override
  public Form closeOrOpenOrder(Long id, String username, Boolean state) {
    Form form = getIfEnabled(id, username);
    form.setOpenOrder(state);
    return formRepository.save(form);
  }

  @Override
  public Long cleanDatabase(LocalDate date) {
    return formRepository.deleteByIsEnabledFalseAndDisabledDateLessThan(date);
  }

}
