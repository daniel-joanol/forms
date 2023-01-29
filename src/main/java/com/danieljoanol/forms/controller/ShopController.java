package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.assembler.ShopAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ShopDTO;
import com.danieljoanol.forms.entity.Shop;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.service.ShopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.SHOP)
@RequiredArgsConstructor
@SecurityRequirement(name = "{bearer.name}")
@PreAuthorize("hasRole('ROLE_USER')")
public class ShopController {

    private final ShopService shopService;
    private final ShopAssembler shopAssembler;

    @Operation(summary = "Create", description = "Method to create a new shop")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ShopDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/")
    public ResponseEntity<ShopDTO> create(@RequestBody @Valid ShopDTO request) {
        String username = JwtTokenUtil.getUsername();
        Shop entity = shopAssembler.convertFromDTO(request);
        ShopDTO response = shopAssembler.convertToDTO(shopService.create(entity, username));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
