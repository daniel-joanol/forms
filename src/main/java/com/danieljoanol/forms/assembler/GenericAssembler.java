package com.danieljoanol.forms.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.danieljoanol.forms.dto.GenericDTO;

public class GenericAssembler<T, U extends GenericDTO> {

    public U convertToDTO(T container) {
        return null;
    }

    public List<U> convertToDTO(List<T> containers) {
        return containers.stream().map(c -> convertToDTO(c))
                .collect(Collectors.toList());
    }

    public Page<U> convertToDTO(Page<T> containers) {
        List<U> listU = convertToDTO(containers.getContent());
        return new PageImpl<>(listU);
    }

    public T convertFromDTO(U containerVO) {
        return (T) containerVO.toEntity();
    }

    public List<T> convertFromDTO(List<U> containers) {
        return containers.stream().map(c -> convertFromDTO(c))
                .collect(Collectors.toList());
    }
}
