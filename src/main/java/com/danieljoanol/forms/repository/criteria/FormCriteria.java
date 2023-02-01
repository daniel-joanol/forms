package com.danieljoanol.forms.repository.criteria;

import java.time.LocalDateTime;

import com.danieljoanol.forms.entity.Group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FormCriteria {

  private String plate;
  private String model;
  private String brand;
  private String frame;
  private String agent;
  private LocalDateTime maxDate;
  private LocalDateTime minDate;
  private Boolean openOrder;
  private Group group;
  private Boolean isEnabled;

}