package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(description = "CRUD about all logic events", name = "Events")
@RestController
@RequestMapping("/events")
public class EventController {

    private EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping("/")
    @Operation(summary = "Save event", description = "return true the registered event")
    public boolean save(@RequestBody Event event) {
        return service.saveEvent(event);
    }

    @GetMapping("/")
    @Operation(summary = "Get all events", description = "Return all registered events")
    public List<Event> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the Id enter", description = "Return the event entered by id")
    public Optional<Event> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a event", description = "Return true if the event exist")
    public boolean updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return service.update(id, event);
    }

}
