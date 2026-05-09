package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.validation.ValidationService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public final class ImpVenueRepository implements VenueRepository {
    private final List<Venue> venues= new ArrayList<>();
    private final ValidationService<Venue> validation;

    public ImpVenueRepository(ValidationService<Venue> validation) {
        this.validation = validation;
    }

    @Override
    public List<Venue> findAll() {
        validation.ListValidation(venues);
        return venues;
    }

    @Override
    public boolean save(Venue venue) {
        validation.uniqueId(venue.getId(),venues,Venue::getId);
        return venues.add(venue);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venues.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    @Override
    public boolean update(Long id, Venue venue) {
        return findById(id).map(v ->
                {
                   v.setName(venue.getName());
                   v.setCity(venue.getCity());
                   return true;
                }
        ).orElse(false);
    }

}
