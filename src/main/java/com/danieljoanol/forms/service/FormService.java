package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Form;

public interface FormService extends GenericService<Form> {

  public Form create(Form form, Long clientId, String username);

  public void deleteAll(Iterable<? extends Form> forms);

  public Page<Form> findByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String plate, String model, String brand, String frame, String agent, LocalDateTime minDate,
      LocalDateTime maxDate, Boolean openOrder);

  public Form closeOrOpenOrder(Long id, String username, Boolean state);

}
