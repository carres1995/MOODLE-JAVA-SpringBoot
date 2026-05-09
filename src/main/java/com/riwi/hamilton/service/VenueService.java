package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.repository.ImpVenueRepository;
import com.riwi.hamilton.repository.VenueRepository;
import com.riwi.hamilton.validation.ValidationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {
    private final ImpVenueRepository repository;
    private final ValidationService<Venue> validation;
    public VenueService(ImpVenueRepository repository, ValidationService<Venue> validation){
        this.repository =repository;
        this.validation = validation;
    }

    public boolean createVenue(Venue venue){
        validation.ObjectExist(venue);
        return repository.save(venue);
    }
    public List<Venue> findAllVenues(){
        return repository.findAll();
    }
    public Optional<Venue> getById(Long id){
        validation.idExist(id);
        return repository.findById(id);
    }
    public boolean update(Long id, Venue venue) {
        validation.idExist(id);
        validation.ObjectExist(venue);
        return repository.update(id, venue);
    }
}
