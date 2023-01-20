package com.danieljoanol.forms.entity;

public interface GenericEntity<T> {
    Long getId();
    void setId(Long id);
    boolean isEnabled();
    void setEnabled(boolean enabled);
}
