package com.danieljoanol.forms.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danieljoanol.forms.constants.Url;
import com.danieljoanol.forms.controller.request.user.CodeConfirmationRequest;
import com.danieljoanol.forms.controller.request.user.NamesUpdateRequest;
import com.danieljoanol.forms.controller.request.user.PasswordUpdateRequest;
import com.danieljoanol.forms.controller.request.user.UsernameUpdateRequest;
import com.danieljoanol.forms.controller.response.PublicUserResponse;
import com.danieljoanol.forms.exception.CodeException;
import com.danieljoanol.forms.service.UserService;
import com.sparkpost.exception.SparkPostException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Url.USER)
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ROLE_USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get", description = "Method to get user")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PublicUserResponse.class)))
    @ApiResponse(responseCode = "500", description = "System error")
    @GetMapping("/{id}")
    public ResponseEntity<PublicUserResponse> get(@PathVariable Long id) {
        PublicUserResponse response = new PublicUserResponse(userService.get(id));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update names", description = "Method to update first and last name")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PublicUserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/names")
    public ResponseEntity<PublicUserResponse> updateNames(@RequestBody(required = true) @Valid NamesUpdateRequest request) {
        PublicUserResponse response = new PublicUserResponse(userService.updateNames(request));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Generate password code", description = "Method to generate code for new password")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/new/password")
    public ResponseEntity<String> newPassword(@RequestBody(required = true) @Valid PasswordUpdateRequest request)
            throws SparkPostException {
        String response = userService.generatePasswordCode(request);
        return ResponseEntity.ok().header("Content-Type", "application/text").body(response);
    }

    @Operation(summary = "Generate username code", description = "Method to generate code for new username")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/new/username")
    public ResponseEntity<String> newUsername(@RequestBody(required = true) @Valid UsernameUpdateRequest request)
            throws SparkPostException {
        String response = userService.generateUsernameCode(request);
        return ResponseEntity.ok().header("Content-Type", "application/text").body(response);
    }

    @Operation(summary = "Confirm new password", description = "Method to confirm code for new password")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/confirm/password")
    public ResponseEntity<String> confirmPassword(@RequestBody(required = true) @Valid CodeConfirmationRequest request)
            throws CodeException {
        String response = userService.confirmNewPassword(request);
        return ResponseEntity.ok().header("Content-Type", "application/text").body(response);
    }

    @Operation(summary = "Confirm new username", description = "Method to confirm code for new username")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PublicUserResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "System error")
    @PutMapping("/confirm/username")
    public ResponseEntity<PublicUserResponse> confirmUsername(@RequestBody(required = true) @Valid CodeConfirmationRequest request)
            throws CodeException {
        PublicUserResponse response = new PublicUserResponse(userService.confirmNewUsername(request));
        return ResponseEntity.ok(response);
    }
}
