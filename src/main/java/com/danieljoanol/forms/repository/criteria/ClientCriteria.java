package com.danieljoanol.forms.repository.criteria;

import com.danieljoanol.forms.entity.Group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClientCriteria {
  
  private String name;
  private String city;
  private String province;
  private String phone;
  private String email;
  private String document;
  private Group group;
  private Boolean isEnabled;

}
