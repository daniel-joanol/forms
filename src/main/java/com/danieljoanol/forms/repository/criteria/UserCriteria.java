package com.danieljoanol.forms.repository.criteria;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserCriteria {
  
  private String firstName;
  private String lastName;
  private String username;
  private LocalDate minLastPayment;
  private LocalDate maxLastPayment;
  private Boolean isEnabled;
  private LocalDate minDisabledDate;
  private LocalDate maxDisabledDate;
  private String groupName;

}