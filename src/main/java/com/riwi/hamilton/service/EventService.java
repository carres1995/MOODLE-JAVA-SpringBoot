package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.repository.EventRepository;
import com.riwi.hamilton.validation.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.by;

@Service
@Transactional
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final ValidationService<Event> validation;

    @Transactional(readOnly = true)
    public Page<EventVenueDTO> getAll(int page, int size) {
        final Pageable pageable = PageRequest.of(
                page, size, Sort.by("name").ascending()
        );
        return repository.findEventsWithVenues(pageable);
    }

    public Event saveEvent(Event event) {
        validation.ObjectExist(event);
        return repository.save(event);
    }

    @Transactional(readOnly = true)
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

    public boolean softDelete(Long id) {
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Event> search(String name) {
        return repository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public Page<Event> ListEvents(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
