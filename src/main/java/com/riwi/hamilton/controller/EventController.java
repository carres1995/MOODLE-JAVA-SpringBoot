package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.req.EventUpdateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.validation.groups.CreateGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    @PostMapping("/")
    @Operation(summary = "Save event", description = "Returns 201 Created and the registered event")
    public ResponseEntity<EventResDTO> save(@Valid @RequestBody @Validated(CreateGroup.class) EventCreateDTO event) {
        final EventResDTO savedEvent = service.saveEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @GetMapping("/")
    @Operation(summary = "Get all events", description = "Return all registered events with status 200 OK. Soft-deleted events are excluded from this list.")
    public ResponseEntity<Map<String, Object>> getAll(
            @Parameter(description = "Page number to return, zero-based")
            @RequestParam(defaultValue = "0") int page
    ) {
        final Slice<EventResDTO> slice = service.getAll(page);
        final Map<String, Object> response = new HashMap<>();
        response.put("events", slice.getContent());
        response.put("currentPage", slice.getNumber());
        response.put("hasNext", slice.hasNext());
        response.put("hasPrevious", slice.hasPrevious());
        response.put("size", slice.getSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter events", description = "Return events filtered by city, category, or date range; ordered by date descending")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered event page returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request, such as missing date range bounds")
    })
    public ResponseEntity<Map<String, Object>> filter(
            @Parameter(description = "Filter by venue city partial text; case-insensitive match against the venue city enum name")
            @RequestParam(required = false) String city,
            @Parameter(description = "Filter by category name partial text; case-insensitive")
            @RequestParam(required = false) String category,
            @Parameter(description = "Start of the date range (inclusive). Requires endDate.")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "End of the date range (inclusive). Requires startDate.")
            @RequestParam(required = false) String endDate,
            @Parameter(description = "Page number to return, zero-based")
            @RequestParam(defaultValue = "0") int page
    ) {
        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Both startDate and endDate are required for date range filtering."));
        }

        final Slice<EventResDTO> slice;
        if (city != null && !city.isBlank() && category != null && !category.isBlank()) {
            slice = service.searchByCityAndCategory(city, category, page);
        } else if (city != null && !city.isBlank()) {
            slice = service.searchByCity(city, page);
        } else if (category != null) {
            slice = service.searchByCategory(category, page);
        } else if (startDate != null && endDate != null) {
            slice = service.searchByDateRange(startDate, endDate, page);
        } else {
            slice = service.getAll(page);
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("events", slice.getContent());
        response.put("currentPage", slice.getNumber());
        response.put("hasNext", slice.hasNext());
        response.put("hasPrevious", slice.hasPrevious());
        response.put("size", slice.getSize());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the Id enter", description = "Return the event entered by id or 404 Not Found")
    public ResponseEntity<EventResDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
    @GetMapping("/search/{name}")
    @Operation(summary = "Search events by partial name", description = "Return events whose name contains the given text")
    public ResponseEntity<List<EventResDTO>> search(@PathVariable String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an event", description = "Return the updated event or 404 Not Found")
    public ResponseEntity<EventResDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventUpdateDTO event) {
        EventResDTO updatedEvent = service.update(id, event);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Soft-delete an event", description = "Soft-deletes the event by marking it unavailable. Soft-deleted events are excluded from list and filter results.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event successfully soft-deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        if (service.softDelete(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/page")
    @Operation(summary = "List by page", description = "Return by size of consult.")
    public ResponseEntity<Page<EventResDTO>> toList(@PageableDefault(size = 10, sort = "name")Pageable pageable){
        Page<EventResDTO> events = service.ListEvents(pageable);
        if(events.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(events);
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
