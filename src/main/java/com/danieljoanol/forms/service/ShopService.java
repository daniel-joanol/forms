package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Shop;

public interface ShopService extends GenericService<Shop> {
    
    public Shop create(Shop shop);
    
}
