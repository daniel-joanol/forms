package com.danieljoanol.forms.service;

public interface GenericService<T> {
    
    public T get(Long id);

    public T get(Long id, String username);

    public T update(T update);

    public T update(T update, String username);

    public void delete(Long id);

    public void delete(Long id, String username);
    
}
