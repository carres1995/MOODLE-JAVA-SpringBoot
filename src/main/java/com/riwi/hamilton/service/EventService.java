package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.repository.EventRepository;
import com.riwi.hamilton.validation.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final ValidationService<Event> validation;

    public List<Event> getAll() {
        return repository.findAll();
    }

    public Event saveEvent(Event event) {
        validation.ObjectExist(event);
        return repository.save(event);
    }

    public Optional<Event> getById(Long id) {
        validation.idExist(id);
        return repository.findById(id);
    }

    public Event update(Long id, Event event) {
        validation.idExist(id);
        validation.ObjectExist(event);
        return repository.findById(id).map(existingEvent -> {
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            repository.save(existingEvent);
            return event;
        }).orElse(null);
    }

    public boolean delete(Long id) {
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }

    public List<Event> search(String name) {
        return repository.findByNameContaining(name);
    }
    public Page<Event> ListEvents(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
