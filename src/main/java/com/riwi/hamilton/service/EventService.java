package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.repository.EventRepository;
import com.riwi.hamilton.utils.Cities;
import com.riwi.hamilton.validation.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.by;

@Service
@Transactional
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final ValidationService<Event> validation;
    private final static int SIZE = 10;

    @Transactional(readOnly = true)
    public Slice<EventVenueDTO> getAll(int page) {
        final Pageable pageable = PageRequest.of(
                page, SIZE, Sort.by("date").descending()
        );

        return repository.findAllBy(pageable)
                .map(event -> new EventVenueDTO(
                        event.getName(),
                        event.getDate(),
                        event.getVenue().getName(),
                        event.getVenue().getCity(),
                        event.getCategories().stream()
                                .map(Category::getName)
                                .collect(Collectors.toList()),
                        event.getVenue().getId(),
                        event.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toList())
                ));
    }

    public Event saveEvent(Event event) {
        validation.ObjectExist(event);
        return repository.save(event);
    }

    @Transactional(readOnly = true)
    public Optional<Event> getById(Long id) {
        validation.idExist(id);
        return repository.findById(id);
    }

    public Event update(Long id, Event event) {
        validation.idExist(id);
        validation.ObjectExist(event);
        return repository.findById(id).map(existingEvent -> {
            existingEvent.setName(event.getName());
            existingEvent.setDate(event.getDate());
            existingEvent.setVenue(event.getVenue());
            existingEvent.setCategories(event.getCategories());
            repository.save(existingEvent);
            return event;
        }).orElse(null);
    }

    public boolean softDelete(Long id) {
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Event> search(String name) {
        return repository.findByNameContaining(name);
    }

    @Transactional(readOnly = true)
    public Page<Event> ListEvents(Pageable pageable) {
        return repository.findAll(pageable);
    }

    private EventVenueDTO mapToDto(Event event) {
        return new EventVenueDTO(
                event.getName(),
                event.getDate(),
                event.getVenue().getName(),
                event.getVenue().getCity(),
                event.getCategories().stream().map(Category::getName).collect(Collectors.toList()),
                event.getVenue().getId(),
                event.getCategories().stream().map(Category::getId).collect(Collectors.toList())
        );
    }

    private List<Cities> resolveCityEnums(String cityFilter) {
        if (cityFilter == null || cityFilter.isBlank()) {
            return Collections.emptyList();
        }
        String normalized = cityFilter.trim().toLowerCase();
        return Arrays.stream(Cities.values())
                .filter(city -> city.name().toLowerCase().contains(normalized))
                .toList();
    }

    @Transactional(readOnly = true)
    public Slice<EventVenueDTO> searchByCity(String cityFilter, int page) {
        if (cityFilter == null || cityFilter.isBlank()) {
            return getAll(page);
        }
        List<Cities> matchingCities = resolveCityEnums(cityFilter);
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        if (matchingCities.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }
        return repository.findByVenue_CityIn(matchingCities, pageable).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Slice<EventVenueDTO> searchByCategory(String categoryName, int page) {
        if (categoryName == null || categoryName.isBlank()) {
            return getAll(page);
        }
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        return repository.findByCategories_NameContainingIgnoreCase(categoryName, pageable).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Slice<EventVenueDTO> searchByDateRange(String startDate, String endDate, int page) {
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        return repository.findByDateBetweenOrderByDateDesc(startDate, endDate, pageable).map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public Slice<EventVenueDTO> searchByCityAndCategory(String cityFilter, String categoryName, int page) {
        List<Cities> matchingCities = resolveCityEnums(cityFilter);
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        if (matchingCities.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }
        if (categoryName == null || categoryName.isBlank()) {
            return repository.findByVenue_CityIn(matchingCities, pageable).map(this::mapToDto);
        }
        return repository.findByVenue_CityInAndCategories_NameContainingIgnoreCase(matchingCities, categoryName, pageable)
                .map(this::mapToDto);
    }
}
