package com.danieljoanol.forms.service;

import org.springframework.data.domain.Page;

public interface GenericService<T> {
    
    public Page<T> getAll(Integer pageNumber, Integer pageSize);

    public T get(Long id);

    public T update(T update);

    public void delete(Long id);

    public void disable(Long id);
    
}
