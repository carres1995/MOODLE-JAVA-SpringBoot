package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.repository.ImpVenueRepository;
import com.riwi.hamilton.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenueService {
    private final ImpVenueRepository repository;
    public VenueService(ImpVenueRepository repository){
        this.repository =repository;
    }
    public boolean createVenue(Venue venue){

        return repository.save(venue);
    }
    public List<Venue> findAllVenues(){
        return repository.findAll();
    }
    public Optional<Venue> getById(Long id){
        return repository.findById(id);
    }
    public boolean update(Long id, Venue venue) {
        return repository.update(id, venue);
    }
}
