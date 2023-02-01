package com.danieljoanol.forms.service;

public interface GenericService<T> {
    
    public T get(Long id);

    public T update(T update);

    public void delete(Long id);

    public void disable(Long id);
    
}
