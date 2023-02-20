package com.danieljoanol.forms.repository.criteria;

import com.danieljoanol.forms.entity.Group;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopCriteria {
  
  private String shopName;
  private String ownerName;
  private String city;
  private String province;
  private String phone;
  private String document;
  private Group group;

}
