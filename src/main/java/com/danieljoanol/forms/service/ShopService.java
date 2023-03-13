package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopService extends GenericService<Shop> {

  public Shop create(Shop shop, String username);

  public void deleteAll(Iterable<? extends Shop> shops);

  public Page<Shop> findByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String shopName, String ownerName, String city, String province, String phone, String document);

  public List<Shop> findAllByUsername(Long id, String username);

  public List<Shop> findAllByUser(User user);
  
}
