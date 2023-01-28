package com.danieljoanol.forms.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

import com.danieljoanol.forms.assembler.RoleAssembler;
import com.danieljoanol.forms.assembler.UserAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.dto.RoleDTO;
import com.danieljoanol.forms.dto.UserDTO;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.service.RoleService;
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
public class AdministrationController {

    private final UserService userService;
    private final UserAssembler userAssembler;
    private final RoleService roleService;
    private final RoleAssembler roleAssembler;

    @Operation(summary = "Get All Users", description = "Method to get all users")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO[].class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/user/")
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize) {
        Page<UserDTO> response = userAssembler.convertToDTO(userService.getAll(pageNumber, pageSize));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get User", description = "Method to get user")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO response = userAssembler.convertToDTO(userService.get(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create User", description = "Method to register a new user")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PostMapping("/user/new")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid RegisterRequest request)
            throws AccessDeniedException, Exception {
        UserDTO response = userAssembler.convertToDTO(userService.create(request, true));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Enable user", description = "Method to enable a user")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/user/enable/{id}")
    public ResponseEntity<UserDTO> enableUser(@PathVariable Long id) throws UsersLimitException {
        UserDTO response = userAssembler.convertToDTO(userService.enable(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update payment date", description = "Method to update the payment date")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/user/payment/{id}")
    public ResponseEntity<UserDTO> updatePayment(@PathVariable Long id, @RequestParam(required = true) LocalDate date) {
        UserDTO response = userAssembler.convertToDTO(userService.updateLastPayment(id, date));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update comments", description = "Method to update comments")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/user/comments/{id}")
    public ResponseEntity<UserDTO> updateComments(@PathVariable Long id, @RequestBody String comments) {
        UserDTO response = userAssembler.convertToDTO(userService.updateComments(id, comments));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete user", description = "Method to delete a user and every entity he owns (if it's the last user from a group_role")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get All Roles", description = "Method to get all roles")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RoleDTO[].class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/role/")
    public ResponseEntity<Page<RoleDTO>> getRoles(
            @RequestParam(required = true) Integer pageNumber,
            @RequestParam(required = true) Integer pageSize) {
        Page<RoleDTO> response = roleAssembler.convertToDTO(roleService.getAll(pageNumber, pageSize));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Role", description = "Method to get role")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RoleDTO.class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO response = roleAssembler.convertToDTO(roleService.get(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user", description = "Method to update a role")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RoleDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/role/maxUsers/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestParam(required = true) Integer maxUsers)
            throws UsersLimitException {
        RoleDTO response = roleAssembler.convertToDTO(roleService.updateMaxUsers(id, maxUsers));
        return ResponseEntity.ok(response);
    }

}
