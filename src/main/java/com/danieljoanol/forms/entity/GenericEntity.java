package com.danieljoanol.forms.entity;

import java.util.Date;

public interface GenericEntity<T> {
    
    Long getId();
    void setId(Long id);
    
    boolean isEnabled();
    void setEnabled(boolean enabled);
    
    Date getDisabledDate();
    void setDisabledDate(Date date);
}
