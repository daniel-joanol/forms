package com.danieljoanol.forms.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.danieljoanol.forms.entity.Shop;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopDTO extends GenericDTO<Shop> {

    private Long id;

    @NotBlank(message = "shopName es obligatorio")
    private String shopName;

    @NotBlank(message = "ownerName es obligatorio")
    private String ownerName;

    @NotBlank(message = "address es obligatorio")
    private String address;

    @NotNull(message = "postalCode es obligatorio")
    private Integer postalCode;

    @NotBlank(message = "city es obligatorio")
    private String city;

    @NotBlank(message = "province es obligatorio")
    private String province;

    @NotBlank(message = "phone1 es obligatorio")
    private String phone1;
    private String phone2;
    private String fax;

    @NotBlank(message = "document es obligatorio")
    private String document;
    private String logo;
    private Set<ClientDTO> clients;

    public ShopDTO(Shop entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.shopName = entity.getShopName();
            this.ownerName = entity.getOwnerName();
            this.address = entity.getAddress();
            this.postalCode = entity.getPostalCode();
            this.city = entity.getCity();
            this.province = entity.getProvince();
            this.phone1 = entity.getPhone1();
            this.phone2 = entity.getPhone2();
            this.fax = entity.getFax();
            this.document = entity.getDocument();
            this.logo = entity.getLogo();
            this.clients = entity.getClients().stream().map(c -> new ClientDTO(c)).collect(Collectors.toSet());
        }
    }

    @Override
    public Shop toEntity() {
        Shop entity = new Shop();
        entity.setId(this.id);
        entity.setShopName(this.shopName);
        entity.setOwnerName(this.ownerName);
        entity.setAddress(this.address);
        entity.setPostalCode(this.postalCode);
        entity.setCity(this.city);
        entity.setProvince(this.province);
        entity.setPhone1(this.phone1);
        entity.setPhone2(this.phone2);
        entity.setFax(this.fax);
        entity.setDocument(this.document);
        entity.setLogo(this.logo);
        entity.setClients(this.clients.stream().map(ClientDTO::toEntity).collect(Collectors.toSet()));
        return entity;
    }
}
