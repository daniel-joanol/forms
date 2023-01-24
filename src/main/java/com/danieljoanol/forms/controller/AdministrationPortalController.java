package com.danieljoanol.forms.controller;

import java.time.LocalDate;

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

import com.danieljoanol.forms.assembler.UserAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.dto.UserDTO;
import com.danieljoanol.forms.entity.User;
import com.danieljoanol.forms.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Url.PORTAL)
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdministrationPortalController {
    
    private final UserService userService;
    private final UserAssembler userAssembler;

    @Operation(summary = "Get All", description = "Method to get all users")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO[].class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/")
    public ResponseEntity<Page<UserDTO>> getAll(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize) {
        Page<UserDTO> response = userAssembler.convertToDTO(userService.getAll(pageNumber, pageSize));
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Register", description = "Method to register a new user")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/newUser")
    public ResponseEntity<UserDTO> registerNewUser(@RequestBody @Valid UserDTO request) {
        User entity = userAssembler.convertFromDTO(request);
        UserDTO response = userAssembler.convertToDTO(userService.create(entity));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Disable an user", description = "Method to disable user")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @DeleteMapping("/disable/{id}")
    public ResponseEntity<?> disable(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Enable an user", description = "Method to enable a user")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/enable/{id}")
    public ResponseEntity<UserDTO> enable(@PathVariable Long id) {
        UserDTO response = userAssembler.convertToDTO(userService.enable(id));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update payment date", description = "Method to update the payment date")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/payment/{id}")
    public ResponseEntity<UserDTO> payment(@PathVariable Long id, @RequestParam(required = true) LocalDate date) {
        UserDTO response = userAssembler.convertToDTO(userService.updateLastPayment(id, date));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update comments", description = "Method to update comments")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/comments/{id}")
    public ResponseEntity<UserDTO> comments(@PathVariable Long id, @RequestBody String comments) {
        UserDTO response = userAssembler.convertToDTO(userService.updateComments(id, comments));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
