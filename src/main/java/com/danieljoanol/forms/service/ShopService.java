package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopService extends GenericService<Shop> {
    
    public Shop create(Shop shop, String username);

    public Shop updateIfEnabled(Shop shop);

    public Shop enable(Long id);
    
    public void deleteAllByIds(Iterable<? extends Long> ids);

    public Page<Shop> findAllEnabledByUsername(Integer pageNumber, Integer pageSize, String username);

    public List<Shop> findAllByUsername(Long id, String username);

    public List<Shop> findAllByUser(User user);

    public Shop getIfEnabled(Long id, String username);

}
