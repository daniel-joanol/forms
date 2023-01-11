package com.danieljoanol.forms.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

}
