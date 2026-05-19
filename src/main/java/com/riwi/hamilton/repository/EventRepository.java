package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByNameContaining(String name);

}
