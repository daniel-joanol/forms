package com.danieljoanol.forms.dto;

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

    @NotNull(message = "id {err.null}")
    private Long id;

    @NotBlank(message = "shopName {err.blank}")
    private String shopName;

    @NotBlank(message = "ownerName {err.blank}")
    private String ownerName;

    @NotBlank(message = "address {err.blank}")
    private String address;

    @NotNull(message = "postalCode {err.null}")
    private Integer postalCode;

    @NotBlank(message = "city {err.blank}")
    private String city;

    @NotBlank(message = "province {err.blank}")
    private String province;

    @NotBlank(message = "message {err.blank}")
    private String phone1;
    private String phone2;
    private String fax;

    @NotBlank(message = "document {err.blank}")
    private String document;
    private String logo;

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
        }
    }

    @Override
    public Shop toEntity() {
        return Shop.builder()
                .id(id)
                .shopName(shopName)
                .ownerName(ownerName)
                .address(address)
                .postalCode(postalCode)
                .city(city)
                .province(province)
                .phone1(phone1)
                .phone2(phone2)
                .fax(fax)
                .document(document)
                .logo(logo)
                .build();
    }
}
