package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.danieljoanol.forms.entity.Form;

public interface FormRepository extends GenericRepository<Form> {
    
    Page<Form> findByGroup_Users_UsernameInAndIsEnabledTrue(Pageable pageable, List<String> usernames);

    Optional<Form> findByIdAndIsEnabledTrueAndGroup_Users_UsernameIn(Long id, List<String> usernames);

}
