package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;

import java.util.List;
import java.util.Optional;

public sealed interface VenueRepository permits ImpVenueRepository {
    List<Venue> findAll();

    boolean save(Venue Venue);

    Optional<Venue> findById(Long id);

    boolean update(Long id, Venue venue);
}
