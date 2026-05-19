package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.repository.VenueRepository;
import com.riwi.hamilton.validation.ValidationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository repository;
    private final ValidationService<Venue> validation;

    public Venue createVenue(Venue venue) {
        validation.ObjectExist(venue);
        return repository.save(venue);
    }

    public List<Venue> findAllVenues() {
        return repository.findAll();
    }

    public Optional<Venue> getById(Long id) {
        validation.idExist(id);
        return repository.findById(id);
    }

    public Venue update(Long id, Venue venue) {
        validation.idExist(id);
        validation.ObjectExist(venue);
        return repository.findById(id).map(existingVenue -> {
            existingVenue.setName(venue.getName());
            existingVenue.setCity(venue.getCity());
            repository.save(existingVenue);
            return venue;
        }).orElse(null);
    }

    public boolean delete(Long id) {
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }

    public List<Venue> searchName(String name) {
        return repository.findByNameContaining(name);
    }
}
