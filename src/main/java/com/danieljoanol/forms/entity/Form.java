package com.danieljoanol.forms.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "form")
public class Form implements GenericEntity<Form> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;
    private Integer fuel;
    private Integer kilometers;
    private String model;
    private String brand;
    private String frame;
    private String chassis;
    private String code;
    private String agent;
    private LocalDateTime date;
    private Boolean openOrder = true;
    private String comments;
    private String pdfUrl;
    private Set<String> visibleDamages = new HashSet<>();

}
