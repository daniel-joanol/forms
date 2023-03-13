package com.danieljoanol.forms.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ShopRepository;
import com.danieljoanol.forms.repository.criteria.ShopCriteria;
import com.danieljoanol.forms.repository.specification.ShopSpecification;

@Service
public class ShopServiceImpl extends GenericServiceImpl<Shop> implements ShopService {

  private final ShopRepository shopRepository;
  private final GroupService groupService;

  public ShopServiceImpl(ShopRepository shopRepository, GroupService groupService) {
    super(shopRepository);
    this.shopRepository = shopRepository;
    this.groupService = groupService;
  }

  @Override
  public Page<Shop> findByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
      String shopName, String ownerName, String city, String province, String phone, String document) {

    Group group = groupService.getByUsername(username);
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    ShopCriteria criteria = ShopCriteria.builder()
          .shopName(shopName)
          .ownerName(ownerName)
          .city(city)
          .province(province)
          .phone(phone)
          .document(document)
          .group(group)
          .build();

    return shopRepository.findAll(ShopSpecification.search(criteria), pageable);
  }

  @Override
  public Shop create(Shop shop, String username) {

    Group group = groupService.getByUsername(username);

    shop.setGroup(group);
    return shopRepository.save(shop);
  }

  @Override
  public List<Shop> findAllByUser(User user) {
    return shopRepository.findByGroup_UsersIn(List.of(user));
  }

  @Override
  public List<Shop> findAllByUsername(Long id, String username) {
    return shopRepository.findByGroup_Users_UsernameIn(List.of(username));
  }

  @Override
  public Shop get(Long id, String username) {
    return shopRepository.findByIdAndGroup_Users_UsernameIn(id, List.of(username))
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public Shop update(Shop shop, String username) {
    get(shop.getId(), username);
    return shopRepository.save(shop);
  }

  @Override
  public void delete(Long id, String username) {
    Shop shop = get(id, username);
    shopRepository.delete(shop);
  }

  @Override
  public void deleteAll(Iterable<? extends Shop> shops) {
    shopRepository.deleteAll(shops);
  }

}
