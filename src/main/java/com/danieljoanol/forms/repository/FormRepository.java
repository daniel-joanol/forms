package com.danieljoanol.forms.repository;

import java.util.List;
import java.util.Optional;

import com.danieljoanol.forms.entity.Form;

public interface FormRepository extends GenericRepository<Form> {
    
    Optional<Form> findByIdAndGroup_Users_UsernameIn(Long id, List<String> usernames);
}
