package com.danieljoanol.forms.entity;

import java.time.LocalDate;

public interface GenericEntity<T> {
    
    Long getId();
    void setId(Long id);
    
    boolean isEnabled();
    void setEnabled(boolean enabled);
    
    LocalDate getDisabledDate();
    void setDisabledDate(LocalDate date);
}
