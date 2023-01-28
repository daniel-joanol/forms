package com.danieljoanol.forms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danieljoanol.forms.entity.Role;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.repository.ShopRepository;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;

@Service
public class ShopServiceImpl extends GenericServiceImpl<Shop> implements ShopService {
    
    private final ShopRepository shopRepository;
    //private final UserService userService;

    @Value("${forms.app.group}")
    private String groupPrefix;

    public ShopServiceImpl(ShopRepository shopRepository/*, UserService userService*/) {
        super(shopRepository);
        this.shopRepository = shopRepository;
        //this.userService = userService;
    }

    @Override
    public Shop create(Shop shop) {
        
        /*User user = JwtTokenUtil.getUserFromContext(userService);
        Role groupRole = null;
        for (Role role : user.getRoles()) {
            if (role.getName().startsWith(groupPrefix)) {
                groupRole = role;
                break;
            }
        }*/

        shop.setEnabled(true);
        shop = shopRepository.save(shop);

        /*List<User> usersFromGroup = userService.getUsersByRole(List.of(groupRole));
        for (User currentUser : usersFromGroup) {
            currentUser.getShops().add(shop);
            currentUser = userService.update(user);
        }*/
        
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
