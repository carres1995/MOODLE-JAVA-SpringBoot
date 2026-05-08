package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.repository.ImpEventRepository;
import com.riwi.hamilton.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final ImpEventRepository repository;
    private  final ValidationService<Event> validation;

    public EventService(ImpEventRepository repository, ValidationService<Event> validation) {
        this.repository = repository;
        this.validation =validation;
    }

    public List<Event> getAll() {
        return repository.findAll();
    }

    public boolean saveEvent(Event event) {
        if(validation.ObjectExist(event)){
            return false;
        }
        return repository.save(event);
    }

    public Optional<Event> getById(Long id){
        if(validation.idExist(id)){
            return Optional.empty();
        }
        return repository.findById(id);
    }
    public boolean update(Long id, Event event){
        if(validation.idExist(id) && validation.ObjectExist(event)) {
            return false;
        }
        return repository.update(id, event);
    }

}
