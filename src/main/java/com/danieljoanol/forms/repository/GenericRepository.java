package com.danieljoanol.forms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.danieljoanol.forms.entity.GenericEntity;

@NoRepositoryBean
public interface GenericRepository <T extends GenericEntity<T>> extends JpaRepository<T, Long> {
    
}
