package com.danieljoanol.forms.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ShopRepository;

@Service
public class ShopServiceImpl extends GenericServiceImpl<Shop> implements ShopService {
    
    private final ShopRepository shopRepository;
    private final UserService userService;

    public ShopServiceImpl(ShopRepository shopRepository, UserService userService) {
        super(shopRepository);
        this.shopRepository = shopRepository;
        this.userService = userService;
    }

    @Override
    public Shop create(Shop shop) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findByUsername(username);
        shop.setEnabled(true);
        shop = shopRepository.save(shop);
        user.setShop(shop);
        user = userService.update(user);
        return shop;
    }

}
