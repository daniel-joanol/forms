package com.danieljoanol.forms.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.danieljoanol.forms.entity.GenericEntity;
import com.danieljoanol.forms.repository.GenericRepository;

public abstract class GenericServiceImpl<T extends GenericEntity<T>> {
    
    public final GenericRepository<T> repository;

    public GenericServiceImpl(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public Page<T> getAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findAll(pageable);
    }

    public T get(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Id " + id + " not found"));
    }

    public T update(T update) {
        return repository.save(update);
    }

    public T create(T create) {
        return repository.save(create);
    }

    public List<T> create(List<T> tList) {
        List<T> response = new ArrayList<>();
        for (T t : tList) {
            response.add(create(t));
        }
        return response;
    }

    public void delete(Long id) {
        // Check if the entity exists
        get(id);
        repository.deleteById(id);
    }

}
