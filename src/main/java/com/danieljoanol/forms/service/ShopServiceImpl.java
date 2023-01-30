package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Group;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.repository.ShopRepository;

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
    public Shop create(Shop shop, String username) {

        Group group = groupService.getByUsernameIn(List.of(username));
        
        shop.setEnabled(true);
        shop = shopRepository.save(shop);
        
        group.getShops().add(shop);
        group = groupService.update(group);
        
        return shop;
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
