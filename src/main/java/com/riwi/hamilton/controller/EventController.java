package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(description = "CRUD about all logic events", name = "Events")
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    @PostMapping("/")
    @Operation(summary = "Save event", description = "Returns 201 Created and the registered event")
    public ResponseEntity<Event> save(@Valid @RequestBody Event event) {
        Event savedEvent = service.saveEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping("/")
    @Operation(summary = "Get all events", description = "Return all registered events with status 200 OK")
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the Id enter", description = "Return the event entered by id or 404 Not Found")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/search/{name}")
    @Operation(summary = "Search event by name", description = "Return the event entered by name or 404 Not Found")
    public ResponseEntity<List<Event>> search(@PathVariable String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a event", description = "Return the updated event or 404 Not Found")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        Event updatedEvent = service.update(id, event);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a event", description = "Return true if the event exist")
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
