package com.danieljoanol.forms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ShopRepository;

@ExtendWith(MockitoExtension.class)
public class ShopServiceImplTest {
  
  @Mock
  private ShopRepository shopRepository;

  @Mock
  private GroupService groupService;

  @InjectMocks
  private ShopServiceImpl service;

  private static final EasyRandom generator = new EasyRandom();
  private static final String STR_VALUE = "string";
  private static final Integer INT_VALUE = 10;
  private static final Long LONG_VALUE = 10l;

  @Test
  void whenFindByUsernameAndFilters_thenReturnShop() {

    Group group = generator.nextObject(Group.class);
    List<Shop> shops = generator.objects(Shop.class, 5).toList();
    Page<Shop> page = new PageImpl<>(shops);

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(shopRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

    final Page<Shop> response = service.findByUsernameAndFilters(
        INT_VALUE, INT_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE, STR_VALUE);
    assertEquals(shops.size(), response.getNumberOfElements());
    assertEquals(1, response.getTotalPages());
  }

  @Test
  void whenCreate_thenReturnShop() {

    Group group = generator.nextObject(Group.class);
    Shop shop = generator.nextObject(Shop.class);

    when(groupService.getByUsername(anyString())).thenReturn(group);
    when(shopRepository.save(any(Shop.class))).thenReturn(shop);

    final Shop response = service.create(shop, STR_VALUE);
    assertEquals(shop.getAddress(), response.getAddress());
  }

  @Test
  void whenFindAllByUser_thenReturnList() {

    List<Shop> shops = generator.objects(Shop.class, 5).toList();
    User user = generator.nextObject(User.class);

    when(shopRepository.findByGroup_UsersIn(anyList())).thenReturn(shops);

    final List<Shop> response = service.findAllByUser(user);
    assertEquals(shops.size(), response.size());
  }

  @Test
  void whenFindAllByUsername_thenReturnList() {

    List<Shop> shops = generator.objects(Shop.class, 5).toList();

    when(shopRepository.findByGroup_Users_UsernameIn(anyList())).thenReturn(shops);

    final List<Shop> response = service.findAllByUsername(LONG_VALUE, STR_VALUE);
    assertEquals(shops.size(), response.size());
  }

  @Test
  void whenGet_thenReturnShop() {

    Shop shop = generator.nextObject(Shop.class);

    when(shopRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(shop));

    final Shop response = service.get(LONG_VALUE, STR_VALUE);
    assertEquals(shop.getDocument(), response.getDocument());
  }

  @Test
  void whenGet_thenThrowException() {

    when(shopRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.empty());

    final EntityNotFoundException thrown = assertThrows(
        EntityNotFoundException.class,
        () -> service.get(LONG_VALUE, STR_VALUE));
    assertEquals(Message.ENTITY_NOT_FOUND, thrown.getMessage());
  }

  @Test
  void whenUpdate_thenReturnShop() {

    Shop shop = generator.nextObject(Shop.class);

    when(shopRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(shop));
    when(shopRepository.save(any(Shop.class))).thenReturn(shop);

    final Shop response = service.update(shop, STR_VALUE);
    assertEquals(shop.getDocument(), response.getDocument());
  }

  @Test
  void whenDelete_thenVerify() {

    Shop shop = generator.nextObject(Shop.class);

    when(shopRepository.findByIdAndGroup_Users_UsernameIn(anyLong(), anyList())).thenReturn(Optional.of(shop));
    
    service.delete(LONG_VALUE, STR_VALUE);
    verify(shopRepository, times(1)).delete(any(Shop.class));
  }

  @Test
  void whenDeleteAll_thenVerify() {

    List<Shop> shops = generator.objects(Shop.class, 5).toList();

    service.deleteAll(shops);
    verify(shopRepository, times(1)).deleteAll(anyList());
  }

}
