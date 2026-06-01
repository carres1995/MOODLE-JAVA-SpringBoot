package com.riwi.hamilton.controller.ui;

import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.service.CategoryService;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.service.VenueService;
import com.riwi.hamilton.utils.Cities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventUIController {

    private final EventService service;
    private final VenueService venueService;
    private final CategoryService categoryService;

    @GetMapping
    public String showEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) String city,
                             @RequestParam(required = false) String category,
                             @RequestParam(required = false) String startDate,
                             @RequestParam(required = false) String endDate,
                             Model model) {

        Slice<EventVenueDTO> events;
        if (city != null && !city.isBlank() && category != null && !category.isBlank()) {
            events = service.searchByCityAndCategory(city, category, page);
        } else if (city != null && !city.isBlank()) {
            events = service.searchByCity(city, page);
        } else if (category != null && !category.isBlank()) {
            events = service.searchByCategory(category, page);
        } else if (startDate != null && endDate != null && !startDate.isBlank() && !endDate.isBlank()) {
            events = service.searchByDateRange(startDate, endDate, page);
        } else {
            events = service.getAll(page);
        }

        model.addAttribute("events", events);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("city", city);
        model.addAttribute("category", category);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("eventForm", new EventVenueDTO("", "", null, null, List.of(), null, List.of()));
        model.addAttribute("venues", venueService.findAllVenues());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("cities", Cities.values());
        return "events";
    }

    @PostMapping("/save")
    public String saveEvent(
            @Valid @ModelAttribute("eventForm") EventVenueDTO form,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("events", service.getAll(0));
            model.addAttribute("venues", venueService.findAllVenues());
            model.addAttribute("categories", categoryService.findAllCategories());
            return "events";
        }

        Venue venue = venueService.getById(form.venueId())
                .orElseThrow(() -> new IllegalArgumentException("Venue does not exist"));

        Set<Category> categories = new HashSet<>(categoryService.findAllByIds(form.categoryIds()));

        Event event = new Event();
        event.setName(form.eventName());
        event.setDate(form.eventDate());
        event.setVenue(venue);
        event.setCategories(categories);

        service.saveEvent(event);

        return "redirect:/admin/events";
    }
}
