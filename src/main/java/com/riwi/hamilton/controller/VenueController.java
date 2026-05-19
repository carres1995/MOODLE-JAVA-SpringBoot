package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(description = "CRUD about all logic venues", name = "Venues")
@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {
    private final VenueService service;

    @GetMapping("/")
    @Operation(summary = "Get all venues", description = "Return all registered venues")
    public ResponseEntity<List<Venue>> getAll() {
        List<Venue> list = service.findAllVenues();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Save venue", description = "return true the registered venue")
    @PostMapping("/")
    public ResponseEntity<Venue> save( @Valid @RequestBody Venue venue) {
        Venue saveVenue = service.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveVenue);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the venue by ID", description = "Return the venue entered by id")
    public ResponseEntity<Venue> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{name}")
    @Operation(summary = "Search event by name", description = "Return the event entered by name or 404 Not Found")
    public ResponseEntity<List<Venue>> search(@PathVariable String name) {
        return ResponseEntity.ok(service.searchName(name));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a venue", description = "Return the updated venue or 404 Not Found")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @Valid @RequestBody Venue venue) {
        Venue updatedVenue = service.update(id, venue);
        if (updatedVenue != null) {
            return ResponseEntity.ok(updatedVenue);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a event", description = "Return true if the vebue exist")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
