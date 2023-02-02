package com.danieljoanol.forms.controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.danieljoanol.forms.assembler.GroupAssembler;
import com.danieljoanol.forms.assembler.UserAssembler;
import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.controller.request.RegisterRequest;
import com.danieljoanol.forms.dto.GroupDTO;
import com.danieljoanol.forms.dto.UserDTO;
import com.danieljoanol.forms.exception.UsersLimitException;
import com.danieljoanol.forms.service.GroupService;
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
  private final GroupService groupService;
  private final GroupAssembler groupAssembler;

  @Operation(summary = "Get All Users", description = "Method to get all users")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/user/")
  public ResponseEntity<Page<UserDTO>> getUsers(
      @RequestParam(required = true) Integer pageNumber,
      @RequestParam(required = true) Integer pageSize,
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String lastName,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String groupName,
      @RequestParam(required = false) Boolean isEnabled,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minLastPayment,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxLastPayment,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDisabledDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDisabledDate) {

    Page<UserDTO> response = userAssembler.convertToDTO(userService.getAll(
        pageNumber, pageSize, firstName, lastName, username, minLastPayment, maxLastPayment, isEnabled, minDisabledDate,
        maxDisabledDate, groupName));
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
  public ResponseEntity<UserDTO> updatePayment(@PathVariable Long id,
      @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
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

  @Operation(summary = "Get All Groups", description = "Method to get all groups")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GroupDTO[].class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/group/")
  public ResponseEntity<Page<GroupDTO>> getGroups(
      @RequestParam(required = true) Integer pageNumber,
      @RequestParam(required = true) Integer pageSize,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer maxUsers,
      @RequestParam(required = false) Integer totalUsers,
      @RequestParam(required = false) String username) {

    Page<GroupDTO> response = groupAssembler.convertToDTO(groupService.getAll(
        pageNumber, pageSize, name, maxUsers, totalUsers, username));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Get Group", description = "Method to get group")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GroupDTO.class)))
  @ApiResponse(responseCode = "500", description = "System error")
  @GetMapping("/group/{id}")
  public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id) {
    GroupDTO response = groupAssembler.convertToDTO(groupService.get(id));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Update group", description = "Method to update a group")
  @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GroupDTO.class)))
  @ApiResponse(responseCode = "400", description = "Bad request")
  @ApiResponse(responseCode = "500", description = "System error")
  @PutMapping("/group/maxUsers/{id}")
  public ResponseEntity<GroupDTO> updateRole(@PathVariable Long id, @RequestParam(required = true) Integer maxUsers)
      throws UsersLimitException {
    GroupDTO response = groupAssembler.convertToDTO(groupService.updateMaxUsers(id, maxUsers));
    return ResponseEntity.ok(response);
  }

  // TODO: disable user
  // TODO: disable users by group
  // TODO: delete users by group

}
