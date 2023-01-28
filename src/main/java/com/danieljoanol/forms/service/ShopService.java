package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Shop;

public interface ShopService extends GenericService<Shop> {
    
    public Shop create(Shop shop, String username);

    public Shop updateIfEnabled(Shop shop);

    public Shop enable(Long id);
    
    public void deleteAllByIds(Iterable<? extends Long> ids);

}
