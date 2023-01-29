package com.danieljoanol.forms.dto;

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
            this.email = entity.getEmail();
        }
    }

    @Override
    public Client toEntity() {
        return Client.builder()
                .id(id)
                .address(address)
                .postalCode(postalCode)
                .city(city)
                .province(province)
                .phone1(phone1)
                .phone2(phone2)
                .document(document)
                .email(email)
                .build();
    }
}
