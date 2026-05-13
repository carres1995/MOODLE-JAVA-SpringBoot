package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;

import java.util.List;
import java.util.Optional;

public sealed interface EventRepository permits ImpEventRepository {
    List<Event> findAll();

    boolean save(Event event);

    Optional<Event> findById(Long id);

    boolean update(Long id, Event event);

}
