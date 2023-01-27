package com.danieljoanol.forms.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.danieljoanol.forms.entity.Form;
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
public class FormDTO extends GenericDTO<Form> {

    @NotNull(message = "id {err.null}")
    private Long id;

    @NotBlank(message = "plate {err.blank}")
    private String plate;

    private Integer fuel;
    private Integer kilometers;

    @NotBlank(message = "model {err.blank}")
    private String model;

    @NotBlank(message = "brand {err.blank}")
    private String brand;

    private String frame;

    @NotBlank(message = "chassis {err.blank}")
    private String chassis;
    private String agent;

    @NotNull(message = "date {err.null}")
    private LocalDateTime date;

    private Boolean openOrder;
    private String comments;

    @NotBlank(message = "pdfUrl {err.blank}")
    private String pdfUrl;

    private Set<String> visibleDamages;

    public FormDTO(Form entity) {
        if (entity != null) {
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
    }

    @Override
    public Form toEntity() {
        return Form.builder()
                .id(id)
                .plate(plate)
                .fuel(fuel)
                .kilometers(kilometers)
                .model(model)
                .brand(brand)
                .frame(frame)
                .chassis(chassis)
                .agent(agent)
                .date(date)
                .openOrder(openOrder)
                .comments(comments)
                .pdfUrl(pdfUrl)
                .visibleDamages(visibleDamages)
                .build();
    }

}
