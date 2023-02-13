package com.danieljoanol.forms.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.danieljoanol.forms.assembler.FormAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.FormDTO;
import com.danieljoanol.forms.entity.Form;
import com.danieljoanol.forms.security.jwt.JwtTokenUtil;
import com.danieljoanol.forms.service.FormService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping(Url.FORM)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_USER')")
public class FormController {

  private final FormService formService;
  private final FormAssembler formAssembler;

  @Operation(summary = "Get All Forms", description = "Method to get all active forms from the group")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/")
  public ResponseEntity<Page<FormDTO>> getAll(
      @RequestParam(required = true) Integer pageNumber,
      @RequestParam(required = true) Integer pageSize,
      @RequestParam(required = false) String plate,
      @RequestParam(required = false) String model,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String frame,
      @RequestParam(required = false) String agent,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxDateTime,
      @RequestParam(required = false) Boolean openOrder) {

    String username = JwtTokenUtil.getUsername();
    Page<FormDTO> response = formAssembler.convertToDTO(formService.findByUsernameAndFilters(
        pageNumber, pageSize, username, plate, model, brand, frame, agent, minDate, maxDateTime, openOrder));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Form", description = "Method to get an active form from the group by id")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/{id}")
  public ResponseEntity<FormDTO> get(
      @PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    FormDTO response = formAssembler.convertToDTO(formService.get(id, username));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Create", description = "Method to create a new form")
  @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PostMapping("/{clientId}/")
  public ResponseEntity<FormDTO> create(@RequestBody @Valid FormDTO request, @PathVariable Long clientId) {
    String username = JwtTokenUtil.getUsername();
    Form entity = formAssembler.convertFromDTO(request);
    FormDTO response = formAssembler.convertToDTO(formService.create(entity, clientId, username));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Open/Close", description = "Method open or close an order (form)")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FormDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PutMapping("/{id}/")
  public ResponseEntity<FormDTO> openOrClose(
      @PathVariable Long id,
      @RequestParam(required = true) Boolean state) {
    String username = JwtTokenUtil.getUsername();
    FormDTO response = formAssembler.convertToDTO(formService.closeOrOpenOrder(id, username, state));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Disable", description = "Method to disable a Form")
  @ApiResponse(responseCode = "204", description = "No content")
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    String username = JwtTokenUtil.getUsername();
    formService.delete(id, username);
    return ResponseEntity.noContent().build();
  }

}
