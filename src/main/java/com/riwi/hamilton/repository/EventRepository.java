package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.utils.Cities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByNameContaining(String name);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Page<Event> findAllBy(Pageable pageable);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Slice<Event> findByVenue_CityIn(List<Cities> cities, Pageable pageable);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Slice<Event> findByCategories_NameContainingIgnoreCase(String categoryName, Pageable pageable);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Slice<Event> findByDateBetweenOrderByDateDesc(String startDate, String endDate, Pageable pageable);

    @EntityGraph(attributePaths = {"venue", "categories"})
    Slice<Event> findByVenue_CityInAndCategories_NameContainingIgnoreCase(List<Cities> cities, String categoryName, Pageable pageable);

    // NOTE: The following method depends on a `capacity` field on `Venue`.
    // Uncomment and implement `capacity` in `Venue` if you want this filter.
    /*
    @EntityGraph(attributePaths = {"venue", "categories"})
    Slice<Event> findByVenue_CapacityGreaterThanEqualOrderByDateDesc(int capacity, Pageable pageable);
    */
}
