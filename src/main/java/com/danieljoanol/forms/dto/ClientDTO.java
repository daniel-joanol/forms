package com.danieljoanol.forms.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.danieljoanol.forms.entity.Client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClientDTO extends GenericDTO<Client> {
    
    private Long id;
    private String address;
    private Integer postalCode;
    private String city;
    private String province;
    private String phone1;
    private String phone2;
    private String document;
    private List<FormDTO> forms;

    public ClientDTO(Client entity) {
        this.id = entity.getId();
        this.address = entity.getAddress();
        this.postalCode = entity.getPostalCode();
        this.city = entity.getCity();
        this.province = entity.getProvince();
        this.phone1 = entity.getPhone1();
        this.phone2 = entity.getPhone2();
        this.document = entity.getDocument();
        this.forms = entity.getForms().stream().map(f -> new FormDTO(f)).collect(Collectors.toList());
    }

    @Override
    public Client toEntity() {
        Client entity = new Client();
        entity.setId(this.id);
        entity.setAddress(this.address);
        entity.setPostalCode(this.postalCode);
        entity.setCity(this.city);
        entity.setProvince(this.province);
        entity.setPhone1(this.phone1);
        entity.setPhone2(this.phone2);
        entity.setDocument(this.document);
        entity.setForms(this.forms.stream().map(FormDTO::toEntity).collect(Collectors.toList()));
        return entity;
    }
}
