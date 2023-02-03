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

import com.danieljoanol.forms.assembler.ClientAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.ClientDTO;
import com.danieljoanol.forms.entity.Client;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.service.ClientService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.CLIENT)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_USER')")
public class ClientController {

  private final ClientService clientService;
  private final ClientAssembler clientAssembler;

  @Operation(summary = "Get All Clients", description = "Method to get all active clients from the group")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/")
  public ResponseEntity<Page<ClientDTO>> getAll(
      @RequestParam(required = true) Integer pageNumber,
      @RequestParam(required = true) Integer pageSize,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String province,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String document) {
    String username = JwtTokenUtil.getUsername();
    Page<ClientDTO> response = clientAssembler.convertToDTO(clientService.findAllEnabledByUsernameAndFilters(pageNumber,
        pageSize, username, name, city, province, phone, email, document));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Client", description = "Method to get an active client from the group by id")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/{id}")
  public ResponseEntity<ClientDTO> get(
      @PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    ClientDTO response = clientAssembler.convertToDTO(clientService.getIfEnabled(id, username));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Create", description = "Method to create a new client")
  @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PostMapping("/")
  public ResponseEntity<ClientDTO> create(
      @RequestBody @Valid ClientDTO request) {
    String username = JwtTokenUtil.getUsername();
    Client entity = clientAssembler.convertFromDTO(request);
    ClientDTO response = clientAssembler.convertToDTO(clientService.create(entity, username));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Update", description = "Method to update a new client")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ClientDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PutMapping("/")
  public ResponseEntity<ClientDTO> update(
      @RequestBody @Valid ClientDTO request) {
    String username = JwtTokenUtil.getUsername();
    Client entity = clientAssembler.convertFromDTO(request);
    ClientDTO response = clientAssembler.convertToDTO(clientService.updateIfEnabled(entity, username));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Disable", description = "Method to disable a client")
  @ApiResponse(responseCode = "204", description = "No content")
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> disable(@PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    clientService.disable(id, username);
    return ResponseEntity.noContent().build();
  }

}
