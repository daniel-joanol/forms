package com.danieljoanol.forms.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "shop")
public class Shop implements GenericEntity<Shop> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shopName;
    private String ownerName;
    private String address;
    private Integer postalCode;
    private String city;
    private String province;
    private String phone1;
    private String phone2;
    private String fax;
    private String document;
    private String logo;

    @OneToMany
    private Set<Client> clients = new HashSet<>();

    private boolean isEnabled;
    private Date disabledDate;
}
