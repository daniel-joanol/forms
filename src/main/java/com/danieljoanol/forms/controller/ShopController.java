package com.danieljoanol.forms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.ShopAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ShopDTO;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.repository.ShopRepository;
import com.danieljoanol.forms.service.ShopService;

@RestController
@RequestMapping(Url.SHOP)
public class ShopController extends GenericController<Shop, ShopDTO> {
    
    @Autowired
    private ShopService shopService;

    public ShopController(ShopRepository shopRepository, ShopAssembler shopAssembler) {
        super(shopRepository, shopAssembler);
    }

}
