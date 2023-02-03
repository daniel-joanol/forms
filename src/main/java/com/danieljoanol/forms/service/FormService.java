package com.danieljoanol.forms.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Form;

public interface FormService extends GenericService<Form> {

  public Form create(Form form, Long clientId, String username);

  public void deleteAllByIds(Iterable<? extends Long> ids);

  public Page<Form> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String plate, String model, String brand, String frame, String agent, LocalDateTime minDate,
      LocalDateTime maxDate, Boolean openOrder);

  public Form getIfEnabled(Long id, String username);

  public Form updateIfEnabled(Form shop, String username);

}
