package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(description = "CRUD about all logic events",name = "Events")
@RestController
@RequestMapping("/events")
public class EventController {

    private EventService service;
    public EventController (EventService service){
        this.service = service;
    }
    @PostMapping("/")
    public boolean save (@RequestBody Event event){
        return service.saveEvent(event);
    }
    @GetMapping("/")
    public List<Event> getAll(){
        return service.getAll();
    }
    @GetMapping("/{id}")
    public Optional<Event> getById(@PathVariable Long id){
        return service.getById(id);
    }
    @PutMapping("/update/{id}")
    public boolean updateEvent(@PathVariable Long id, @RequestBody Event event){
        return service.update(id, event);
    }

}
