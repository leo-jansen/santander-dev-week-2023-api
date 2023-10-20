package me.dio.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.dio.controller.dto.AccountDto;
import me.dio.controller.dto.CardDto;
import me.dio.domain.model.Account;
import me.dio.domain.model.Card;
import me.dio.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/card")
@Tag(name = "Card Controller", description = "RESTful API for managing card.")
public record CardController(CardService service) {
    @GetMapping
    @Operation(summary = "Get all card", description = "Retrieve a list of all registered cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")
    })
    public ResponseEntity<List<CardDto>> findAll() {
        var cards = service.findAll();
        var cardDtos = cards.stream().map(CardDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(cardDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a card by ID", description = "Retrieve a specific card based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    public ResponseEntity<CardDto> findById(@PathVariable Long id) {
        Card card = service.findById(id);
        return ResponseEntity.ok(new CardDto(card));
    }

    @PostMapping
    @Operation(summary = "Create a new card", description = "Create a new card and return the created card's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "422", description = "Invalid account data provided")
    })
    public ResponseEntity<CardDto> create(@RequestBody CardDto cardDto) {
        var card = service.create(cardDto.toModel());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(card.getId())
                .toUri();
        return ResponseEntity.created(location).body(new CardDto(card));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a card", description = "Update the data of an existing card based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "422", description = "Invalid card data provided")
    })
    public ResponseEntity<CardDto> update(@PathVariable Long id, @RequestBody CardDto cardDto) {
        var card = service.update(id, cardDto.toModel());
        return ResponseEntity.ok(new CardDto(card));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Card a account", description = "Delete an existing card based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
