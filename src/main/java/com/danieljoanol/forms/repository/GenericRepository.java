package com.danieljoanol.forms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.danieljoanol.forms.entity.GenericEntity;

@NoRepositoryBean
public interface GenericRepository <T extends GenericEntity<T>> extends JpaRepository<T, Long> {
    
    Page<T> findByIsEnabled(Pageable page, boolean enabled);
    Optional<T> findByIdAndIsEnabled(Long id, boolean enabled);
}
