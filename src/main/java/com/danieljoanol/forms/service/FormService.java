package com.danieljoanol.forms.service;

import org.springframework.data.domain.Page;

import com.danieljoanol.forms.entity.Form;

public interface FormService extends GenericService<Form> {
    
    public Form create(Form form, Long clientId, String username);
    
    public Form updateIfEnabled(Form form);
    
    public Form enable(Long id);

    public void deleteAllByIds(Iterable<? extends Long> ids);

    public Page<Form> findAllEnabledByUsername(Integer pageNumber, Integer pageSize, String username);

    public Form getIfEnabled(Long id, String username);

}
