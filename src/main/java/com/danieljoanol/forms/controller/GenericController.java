package com.danieljoanol.forms.controller;

import com.danieljoanol.forms.assembler.GenericAssembler;
import com.danieljoanol.forms.dto.GenericDTO;
import com.danieljoanol.forms.entity.GenericEntity;
import com.danieljoanol.forms.repository.GenericRepository;
import com.danieljoanol.forms.service.GenericServiceImpl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@PreAuthorize("hasRole('USER')")
public abstract class GenericController<T extends GenericEntity<T>, U extends GenericDTO> {
    
    private final GenericServiceImpl<T> service;
    private final GenericAssembler<T, U> assembler;

    public GenericController(
            GenericRepository<T> repository, GenericAssembler<T, U> assembler) {
        this.service = new GenericServiceImpl<T>(repository){};
        this.assembler = assembler;
    }

    @ApiOperation(value = "Retrieve all ", httpMethod = "GET")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/")
    public ResponseEntity<Page<U>> getAll(Integer pageNumber, Integer pageSize) {
        Page<T> entities = service.getAll(pageNumber, pageSize);
        Page<U> vos = this.assembler.convertToDTO(entities);
        return ResponseEntity.ok(vos);
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @GetMapping("/{id}")
    public ResponseEntity<U> getOne(@PathVariable Long id) {
        T entity = service.get(id);
        if (entity == null) {
            return ResponseEntity.noContent().build();
        }

        U vo = this.assembler.convertToDTO(entity);
        return ResponseEntity.ok(vo);
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @PutMapping("/")
    public ResponseEntity<U> update(@RequestBody @Valid U updated) {
        T entity = this.assembler.convertFromDTO(updated);
        U vo = this.assembler.convertToDTO(service.update(entity));
        return ResponseEntity.ok(vo);
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/")
    public ResponseEntity<U> create(@RequestBody @Valid U created) {
        T entity = this.assembler.convertFromDTO(created);
        U vo = this.assembler.convertToDTO(service.create(entity));
        return ResponseEntity.ok(vo);
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @PostMapping("/list")
    public ResponseEntity<List<U>> createWithList(@RequestBody @Valid List<U> tList) {
        List<T> entities = this.assembler.convertFromDTO(tList);
        List<U> vos = this.assembler.convertToDTO(service.create(entities));
        return ResponseEntity.ok(vos);
    }

    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "SUCCESS"), @ApiResponse(code = 500, message = "System error") })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Ok");
    }
}
