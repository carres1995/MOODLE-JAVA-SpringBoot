package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.repository.ImpEventRepository;
import com.riwi.hamilton.validation.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    private final ImpEventRepository repository;
    private final ValidationService<Event> validation;

    public List<Event> getAll() {
        return repository.findAll();
    }

    public boolean saveEvent(Event event) {
        validation.ObjectExist(event);
        return repository.save(event);
    }

    public Optional<Event> getById(Long id) {
        validation.idExist(id);
        return repository.findById(id);
    }

    public boolean update(Long id, Event event) {
        validation.idExist(id);
        validation.ObjectExist(event);
        return repository.update(id, event);
    }

}
