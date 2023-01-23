package com.danieljoanol.forms.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
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
    
    @NotNull(message = "id {err.null}")
    private Long id;

    @NotBlank(message = "address {err.blank}")
    private String address;

    @NotNull(message = "postalCode {err.null}")
    private Integer postalCode;

    @NotBlank(message = "city {err.blank}")
    private String city;

    @NotBlank(message = "province {err.blank}")
    private String province;

    @NotBlank(message = "phone1 {err.blank}")
    private String phone1;

    private String phone2;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            message = "{err.email}")
    private String email;

    @NotBlank(message = "document {err.blank}")
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
            this.email = entity.getEmail();
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
        entity.setEmail(this.email);
        entity.setForms(this.forms.stream().map(FormDTO::toEntity).collect(Collectors.toList()));
        return entity;
    }
}
