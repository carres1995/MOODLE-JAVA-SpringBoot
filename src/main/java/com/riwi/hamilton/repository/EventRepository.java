package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByNameContaining(String name);
    @Query("""
        SELECT new com.riwi.hamilton.model.dto.EventVenueDTO(
            e.name,
            e.date,
            v.name,
            v.city
        )
        FROM Event e
        LEFT JOIN e.venue v
    """)
    Page<EventVenueDTO> findEventsWithVenues(Pageable pageable);

}
