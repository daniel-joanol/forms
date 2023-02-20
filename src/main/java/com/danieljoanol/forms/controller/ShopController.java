package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_USER')")
public class ShopController {

  private final ShopService shopService;
  private final ShopAssembler shopAssembler;

  @Operation(summary = "Get All Shops", description = "Method to get all active shops from the group")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ShopDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/")
  public ResponseEntity<Page<ShopDTO>> getAll(
      @RequestParam(required = true) Integer pageNumber,
      @RequestParam(required = true) Integer pageSize,
      @RequestParam(required = false) String shopName,
      @RequestParam(required = false) String ownerName,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String province,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String document) {
    String username = JwtTokenUtil.getUsername();
    Page<ShopDTO> response = shopAssembler.convertToDTO(shopService.findByUsernameAndFilters(pageNumber,
        pageSize, username, shopName, ownerName, city, province, phone, document));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Shop", description = "Method to get an active shop from the group by id")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ShopDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/{id}")
  public ResponseEntity<ShopDTO> get(
      @PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    ShopDTO response = shopAssembler.convertToDTO(shopService.get(id, username));
    return ResponseEntity.ok(response);
  }

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

  @Operation(summary = "Update", description = "Method to update a new shop")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ShopDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PutMapping("/")
  public ResponseEntity<ShopDTO> update(@RequestBody @Valid ShopDTO request) {
    String username = JwtTokenUtil.getUsername();
    Shop entity = shopAssembler.convertFromDTO(request);
    ShopDTO response = shopAssembler.convertToDTO(shopService.update(entity, username));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Delete", description = "Method to disable a shop")
  @ApiResponse(responseCode = "204", description = "No content")
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    shopService.delete(id, username);
    return ResponseEntity.noContent().build();
  }
}
