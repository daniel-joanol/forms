package com.danieljoanol.forms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
    private String agent;
    private LocalDateTime date;
    // TODO: implement closeOrder, reopen
    private Boolean openOrder = true;
    private String comments;
    private String pdfUrl;

    @ElementCollection
    private Set<String> visibleDamages = new HashSet<>();

    private boolean isEnabled;
    private LocalDate disabledDate;

    @ManyToOne
    private Group group;
}
