package com.danieljoanol.forms.service;

import java.util.List;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;

public interface ShopService extends GenericService<Shop> {
    
    public Shop create(Shop shop, String username);

    public Shop updateIfEnabled(Shop shop);

    public Shop enable(Long id);
    
    public void deleteAllByIds(Iterable<? extends Long> ids);

    public List<Shop> findAllByUsernames(Long id, List<String> usernames);

    public List<Shop> findAllByUsers(List<User> users);
}
