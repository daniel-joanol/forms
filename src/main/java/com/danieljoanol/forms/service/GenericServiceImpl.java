package com.danieljoanol.forms.service;

import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.GenericEntity;
import com.danieljoanol.forms.repository.GenericRepository;

public abstract class GenericServiceImpl<T extends GenericEntity<T>> {
    
    public final GenericRepository<T> repository;

    public GenericServiceImpl(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public Page<T> getAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repository.findByIsEnabled(pageable, true);
    }

    public T get(Long id) {
        return repository.findByIdAndIsEnabled(id, true)
            .orElseThrow(() -> new EntityNotFoundException(Message.ID_NOT_FOUND));
    }

    public T update(T update) {
        return repository.save(update);
    }

    public T create(T create) {
        create.setId(null);
        return repository.save(create);
    }

    public void delete(Long id) {
        T entity = get(id);
        entity.setEnabled(false);
        entity.setDisabledDate(new Date());
        update(entity);
    }

    public T enable(Long id) {
        T entity = get(id);
        entity.setEnabled(true);
        entity.setDisabledDate(null);
        return update(entity);
    }

}
