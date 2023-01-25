package com.danieljoanol.forms.controller;

import com.danieljoanol.forms.assembler.GenericAssembler;
import com.danieljoanol.forms.dto.GenericDTO;
import com.danieljoanol.forms.entity.GenericEntity;
import com.danieljoanol.forms.repository.GenericRepository;
import com.danieljoanol.forms.service.GenericServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasRole('ROLE_USER')")
@SecurityRequirement(name = "Bearer Authentication")
public abstract class GenericController<T extends GenericEntity<T>, U extends GenericDTO<T>> {

    private final GenericServiceImpl<T> service;
    private final GenericAssembler<T, U> assembler;

    public GenericController(
            GenericRepository<T> repository, GenericAssembler<T, U> assembler) {
        this.service = new GenericServiceImpl<T>(repository) {
        };
        this.assembler = assembler;
    }

    @GetMapping("/")
    public ResponseEntity<Page<U>> getAll(@RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize) {
        Page<T> entities = service.getAll(pageNumber, pageSize);
        Page<U> vos = this.assembler.convertToDTO(entities);
        return ResponseEntity.ok(vos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<U> getOne(@PathVariable Long id) {
        T entity = service.get(id);
        if (entity == null) {
            return ResponseEntity.noContent().build();
        }

        U vo = this.assembler.convertToDTO(entity);
        return ResponseEntity.ok(vo);
    }

    @PutMapping("/")
    public ResponseEntity<U> update(@RequestBody @Valid U updated) {
        T entity = this.assembler.convertFromDTO(updated);
        U vo = this.assembler.convertToDTO(service.update(entity));
        return ResponseEntity.ok(vo);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<U> enable(@PathVariable Long id) {
        U vo = this.assembler.convertToDTO(service.enable(id));
        return ResponseEntity.ok(vo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: review get methods
    //TODO: review get all methods
    //TODO: delete methods
    //TODO: review update methods
}
