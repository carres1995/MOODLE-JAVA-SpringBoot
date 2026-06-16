package com.riwi.hamilton.service;

import com.riwi.hamilton.exception.BusinessRuleViolationException;
import com.riwi.hamilton.exception.DuplicateResourceException;
import com.riwi.hamilton.exception.ResourceNotFoundException;
import com.riwi.hamilton.mapper.MapperEvent;
import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.req.EventUpdateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
import com.riwi.hamilton.repository.EventRepository;
import com.riwi.hamilton.utils.Cities;
import com.riwi.hamilton.validation.ValidationService;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.repository.VenueRepository;
import com.riwi.hamilton.repository.CategoryRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalTime.now;
import static org.springframework.data.domain.Sort.by;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository repository;
    private final VenueRepository venueRepository;
    private final CategoryRepository categoryRepository;
    private final ValidationService<Event> validation;
    private final static int SIZE = 10;
    private final MapperEvent mapper;

    @Transactional(readOnly = true)
    public Slice<EventResDTO> getAll(int page) {
        final Pageable pageable = PageRequest.of(
                page, SIZE, Sort.by("date").descending()
        );

        return repository.findAllBy(pageable)
                .map(this.mapper::toResponseDTO);
    }

    public EventResDTO saveEvent(EventCreateDTO dto) {
        Event event = mapper.toEntity(dto);
        if (dto.venueId() != null) {
            Venue venue = venueRepository.findById(dto.venueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with ID " + dto.id()));
            event.setVenue(venue);
        }
        if (dto.categoriesId() != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.categoriesId()));
            event.setCategories(categories);
        }
        if (repository.existsByName(dto.name())) { // Asumiendo que tienes este método en tu Repo
            throw new DuplicateResourceException("An event with the name '" + dto.name() + "' already exists.");
        }
        validation.ObjectExist(event);
        Event saved = repository.save(event);
        return mapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public EventResDTO getById(Long id) {
        validation.idExist(id);
        Event event = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Event not found by ID "+ id));
        return mapper.toResponseDTO(event);
    }

    public EventResDTO update(Long id, EventUpdateDTO dto) {
        validation.idExist(id);
        Event event = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Event not found by ID " + id));
        validation.ObjectExist(event);
        mapper.updateEntityFromDTO(dto, event);
        if (dto.venueId() != null) {
            Venue venue = venueRepository.findById(dto.venueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found by ID" + dto.venueId()));
            event.setVenue(venue);
        }
        if (dto.categoriesId() != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(dto.categoriesId()));
            event.setCategories(categories);
        }
        if (dto.date().isBefore(LocalDate.now())){
            throw new BusinessRuleViolationException("date cant before right now.");
        }
        Event updated = repository.save(event);
        return mapper.toResponseDTO(updated);
    }

    public boolean softDelete(Long id) {
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<EventResDTO> search(String name) {


        return repository.findByNameContaining(name).stream().map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<EventResDTO> ListEvents(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
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
    public Slice<EventResDTO> searchByCity(String cityFilter, int page) {
        if (cityFilter == null || cityFilter.isBlank()) {
            return getAll(page);
        }
        List<Cities> matchingCities = resolveCityEnums(cityFilter);
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        if (matchingCities.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }
        return repository.findByVenue_CityIn(matchingCities, pageable).map(mapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Slice<EventResDTO> searchByCategory(String categoryName, int page) {
        if (categoryName == null || categoryName.isBlank()) {
            return getAll(page);
        }
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        return repository.findByCategories_NameContainingIgnoreCase(categoryName, pageable).map(mapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Slice<EventResDTO> searchByDateRange(String startDate, String endDate, int page) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isAfter(end)) {
            throw new BusinessRuleViolationException("The start date cannot be after the end date.");
        }
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        return repository.findByDateBetweenOrderByDateDesc(start, end, pageable).map(mapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Slice<EventResDTO> searchByCityAndCategory(String cityFilter, String categoryName, int page) {
        List<Cities> matchingCities = resolveCityEnums(cityFilter);
        final Pageable pageable = PageRequest.of(page, SIZE, Sort.by("date").descending());
        if (matchingCities.isEmpty()) {
            return new SliceImpl<>(Collections.emptyList(), pageable, false);
        }
        if (categoryName == null || categoryName.isBlank()) {
            return repository.findByVenue_CityIn(matchingCities, pageable).map(mapper::toResponseDTO);
        }
        return repository.findByVenue_CityInAndCategories_NameContainingIgnoreCase(matchingCities, categoryName, pageable)
                .map(mapper::toResponseDTO);
    }
}
