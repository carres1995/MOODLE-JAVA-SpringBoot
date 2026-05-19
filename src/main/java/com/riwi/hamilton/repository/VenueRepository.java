package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByNameContaining(String name);

}
