package com.danieljoanol.forms.repository.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GroupCriteria {

  private String name;
  private Integer maxUsers;
  private Integer totalUsers;
  private String username;
  
}
