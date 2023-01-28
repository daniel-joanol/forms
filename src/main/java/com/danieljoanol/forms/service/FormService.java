package com.danieljoanol.forms.service;

import com.danieljoanol.forms.entity.Form;

public interface FormService extends GenericService<Form> {
    
    public Form create(Form form, Long clientId, String username);
    
    public Form updateIfEnabled(Form form);
    
    public Form enable(Long id);

    public void deleteAllByIds(Iterable<? extends Long> ids);

}
