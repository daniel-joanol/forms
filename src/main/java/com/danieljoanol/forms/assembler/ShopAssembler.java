package com.danieljoanol.forms.assembler;

import org.springframework.stereotype.Component;

import com.danieljoanol.forms.dto.ShopDTO;
import com.danieljoanol.forms.entity.Shop;

@Component
public class ShopAssembler extends GenericAssembler<Shop, ShopDTO> {

    @Override
    public ShopDTO convertToDTO(Shop entity) {
        return new ShopDTO(entity);
    }
    
}
