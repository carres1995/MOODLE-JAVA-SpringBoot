package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(description = "CRUD about all logic venues",name = "Venues")
@RestController
@RequestMapping("/venues")
public class VenueController {
    private final VenueService service;
    public VenueController(VenueService service){
        this.service = service;
    }

    @GetMapping("/")
    @Operation(summary = "Get all venues", description = "Return all registered venues")
    public List<Venue> getAll (){
        return service.findAllVenues();
    }

    @Operation(summary = "Save venue", description = "return true the registered venue")
    @PostMapping("/")
    public boolean save(@RequestBody Venue venue){
        return service.createVenue(venue);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the venue by ID", description = "Return the venue entered by id")
    public Optional<Venue> getById(@PathVariable Long id){
        return service.getById(id);
    }
    @PutMapping("/update/{id}")
    @Operation(summary = "Update a venue", description = "Return true if the venue exist")
    public boolean updateVenue(@PathVariable Long id, @RequestBody Venue venue){
        return service.update(id, venue);
    }
}
