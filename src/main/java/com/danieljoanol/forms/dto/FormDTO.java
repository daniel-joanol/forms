package com.danieljoanol.forms.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.danieljoanol.forms.entity.Form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FormDTO extends GenericDTO<Form> {
    
    private Long id;

    @NotBlank(message = "plate es obligatorio")
    private String plate;    
    
    private Integer fuel;
    private Integer kilometers;

    @NotBlank(message = "model es obligatorio")
    private String model;

    @NotBlank(message = "brand es obligatorio")
    private String brand;
    
    private String frame;

    @NotBlank(message = "chassis es obligatorio")
    private String chassis;
    private String agent;

    @NotNull(message = "date es obligatorio")
    private LocalDateTime date;
    
    private Boolean openOrder;
    private String comments;

    @NotBlank(message = "pdfUrl es obligatorio")
    private String pdfUrl;

    private Set<String> visibleDamages;

    public FormDTO(Form entity) {
        this.id = entity.getId();
        this.plate = entity.getPlate();
        this.fuel = entity.getFuel();
        this.kilometers = entity.getKilometers();
        this.model = entity.getModel();
        this.brand = entity.getBrand();
        this.frame = entity.getFrame();
        this.chassis = entity.getChassis();
        this.agent = entity.getAgent();
        this.date = entity.getDate();
        this.openOrder = entity.getOpenOrder();
        this.comments = entity.getComments();
        this.pdfUrl = entity.getPdfUrl();
        this.visibleDamages = entity.getVisibleDamages();
    }

    @Override
    public Form toEntity() {
        Form entity = new Form();
        entity.setId(this.id);
        entity.setPlate(this.plate);
        entity.setFuel(this.fuel);
        entity.setKilometers(this.kilometers);
        entity.setModel(this.model);
        entity.setBrand(this.brand);
        entity.setFrame(this.frame);
        entity.setChassis(this.chassis);
        entity.setAgent(this.agent);
        entity.setDate(this.date);
        entity.setOpenOrder(this.openOrder);
        entity.setComments(this.comments);
        entity.setPdfUrl(this.pdfUrl);
        entity.setVisibleDamages(this.visibleDamages);
        return entity;
    }

    
}
