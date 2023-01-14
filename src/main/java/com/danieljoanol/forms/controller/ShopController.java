package com.danieljoanol.forms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.ShopAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ShopDTO;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.service.ShopService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(Url.SHOP)
@Api(value = "Shop Controller", description = "Controller to manage shops")
public class ShopController extends GenericController<Shop, ShopDTO> {
    
    @Autowired
    private ShopService shopService;

    public ShopController(ShopService shopService, ShopAssembler shopAssembler) {
        super(null, shopAssembler);
    }

}
