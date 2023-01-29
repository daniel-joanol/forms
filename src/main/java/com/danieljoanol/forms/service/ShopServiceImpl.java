package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ShopRepository;
import com.danieljoanol.forms.repository.UserRepository;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@Service
public class ShopServiceImpl extends GenericServiceImpl<Shop> implements ShopService {
    
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Value("${forms.app.group}")
    private String GROUP_PREFIX;

    public ShopServiceImpl(ShopRepository shopRepository, UserRepository userRepository) {
        super(shopRepository);
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Shop create(Shop shop, String username) {
        
        String groupName = JwtTokenUtil.getGroupRole(GROUP_PREFIX);
        List<User> users = userRepository.findByRoles_NameIn(List.of(groupName));

        shop.setEnabled(true);
        shop.setClients(users.get(0).getShops().get(0).getClients());
        shop = shopRepository.save(shop);

        for (User user : users) {
            user.getShops().add(shop);
        }
        
        userRepository.saveAll(users);
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
