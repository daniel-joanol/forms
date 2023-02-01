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
  public Page<Shop> findAllEnabledByUsernameAndFilters(Integer pageNumber, Integer pageSize, String username,
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
          .isEnabled(true)
          .build();

    return shopRepository.findAll(ShopSpecification.search(criteria), pageable);
  }

  @Override
  public Shop create(Shop shop, String username) {

    Group group = groupService.getByUsername(username);

    shop.setEnabled(true);
    shop.setGroup(group);
    shop = shopRepository.save(shop);

    return shop;
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
  public Shop getIfEnabled(Long id, String username) {
    return shopRepository.findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(id, List.of(username))
        .orElseThrow(() -> new EntityNotFoundException(Message.ENTITY_NOT_FOUND));
  }

  @Override
  public Shop updateIfEnabled(Shop shop) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void disable(Long id) {
    // TODO Auto-generated method stub

  }

  @Override
  public Shop enable(Long id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(Long id) {
    Shop shop = get(id);
    shopRepository.delete(shop);
  }

  @Override
  public void deleteAllByIds(Iterable<? extends Long> ids) {
    shopRepository.deleteAllById(ids);
  }

}
