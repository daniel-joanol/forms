package com.danieljoanol.forms.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.danieljoanol.forms.entity.Client;
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
public class ClientDTO extends GenericDTO<Client> {
    private Long id;

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

    @NotBlank(message = "document es obligatorio")
    private String document;

    private List<FormDTO> forms;

    public ClientDTO(Client entity) {
        if (entity != null) {
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
