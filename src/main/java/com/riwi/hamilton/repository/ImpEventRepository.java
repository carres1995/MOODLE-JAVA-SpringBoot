package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.validation.ValidationService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public final class ImpEventRepository implements EventRepository {
    private final List<Event> events = new ArrayList<>();
    private final ValidationService<Event> validation;

    public ImpEventRepository(ValidationService<Event> validation) {
        this.validation = validation;
    }

    @Override
    public List<Event> findAll(){
        validation.ListValidation(events);

        return events;
    }

    @Override
    public boolean save(Event event) {
        validation.uniqueId(event.getId(),events,Event::getId);
        events.add(event);
        return true;
    }

    @Override
    public Optional<Event> findById(Long id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public boolean update(Long id, Event event) {
        return findById(id).map(e -> {
                    e.setName(event.getName());
                    e.setDate(event.getDate());
        return true;
        }).orElse(false);
    }
}
