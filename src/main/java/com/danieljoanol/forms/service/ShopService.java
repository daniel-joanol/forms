package com.danieljoanol.forms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopService extends GenericService<Shop> {

  public Shop create(Shop shop, String username);

  public void deleteAllByIds(Iterable<? extends Long> ids);

  public Page<Shop> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String shopName, String ownerName, String city, String province, String phone, String document);

  public List<Shop> findAllByUsername(Long id, String username);

  public List<Shop> findAllByUser(User user);

  public Shop getIfEnabled(Long id, String username);

  public Shop updateIfEnabled(Shop shop, String username);

  public Long cleanDatabase(LocalDate date);
  
}
