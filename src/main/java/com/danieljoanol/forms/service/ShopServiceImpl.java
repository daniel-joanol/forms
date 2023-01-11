package com.danieljoanol.forms.service;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.repository.ShopRepository;

@Service
public class ShopServiceImpl extends GenericServiceImpl<Shop> implements ShopService {
    
    private final ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        super(shopRepository);
        this.shopRepository = shopRepository;
    }

}
