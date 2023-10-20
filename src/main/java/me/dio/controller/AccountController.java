package me.dio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.dio.controller.dto.AccountDto;
import me.dio.domain.model.Account;
import me.dio.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/account")
@Tag(name = "Account Controller", description = "RESTful API for managing account.")
public record AccountController(AccountService service) {
    @GetMapping
    @Operation(summary = "Get all account", description = "Retrieve a list of all registered accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<AccountDto>> findAll() {
        var accounts = service.findAll();
        var accountDtos = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a account by ID", description = "Retrieve a specific account based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountDto> findById(@PathVariable Long id) {
        Account account = service.findById(id);
        return ResponseEntity.ok(new AccountDto(account));
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account and return the created account's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid account data provided")
    })
    public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
        var account = service.create(accountDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return ResponseEntity.created(location).body(new AccountDto(account));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a account", description = "Update the data of an existing account based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "422", description = "Invalid card data provided")
    })
    public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        var account = service.update(id, accountDto.toModel());
        return ResponseEntity.ok(new AccountDto(account));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a account", description = "Delete an existing account based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
