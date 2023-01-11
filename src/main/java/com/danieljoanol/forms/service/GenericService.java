package com.danieljoanol.forms.service;

import java.util.List;

import org.springframework.data.domain.Page;

public interface GenericService<T> {
    
    public Page<T> getAll(Integer pageNumber, Integer pageSize);

    public T get(Long id);

    public T update(T update);

    public T create(T create);

    public List<T> create(List<T> tList);

    public void delete(Long id);

}
