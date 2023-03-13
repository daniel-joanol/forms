package com.danieljoanol.forms.service;

import javax.persistence.EntityNotFoundException;

import com.danieljoanol.forms.constants.Message;
import com.danieljoanol.forms.entity.GenericEntity;
import com.danieljoanol.forms.repository.GenericRepository;

public abstract class GenericServiceImpl<T extends GenericEntity<T>> {

  public final GenericRepository<T> repository;

  public GenericServiceImpl(GenericRepository<T> repository) {
    this.repository = repository;
  }

  public T get(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Message.ID_NOT_FOUND));
  }

  public T update(T update) {
    return repository.save(update);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

}
