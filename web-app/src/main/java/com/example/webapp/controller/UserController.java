package com.example.webapp.controller;

import com.example.api.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final List<UserDto> users = new ArrayList<>();

    public UserController() {
        // Add some sample users
        users.add(UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("user1")
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .build());
                
        users.add(UserDto.builder()
                .id(UUID.randomUUID().toString())
                .username("user2")
                .email("user2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = UserDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get a user by ID", description = "Returns a user based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = UserDto.class)))
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "User to create", required = true) @RequestBody UserDto user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Update an existing user", description = "Updates a user based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully", 
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable String id,
            @Parameter(description = "Updated user details", required = true) @RequestBody UserDto user) {
        
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                user.setId(id);
                user.setUpdatedAt(LocalDateTime.now());
                users.set(i, user);
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a user", description = "Deletes a user based on ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", 
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable String id) {
        
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
