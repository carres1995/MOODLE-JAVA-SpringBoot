package com.riwi.hamilton.controller.ui;

import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.model.dto.EventForm;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventUIController {

    private final EventService service;
    private final VenueService venueService;

    @GetMapping
    public String showEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             Model model) {
        Page<EventVenueDTO> events = service.getAll(page, size);

        model.addAttribute("events", events);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("eventForm", new EventVenueDTO("", "", "", ""));
        model.addAttribute("venues", venueService.findAllVenues());
        return "events";
    }

    @PostMapping("/save")
    public String saveEvent(
            @Valid @ModelAttribute("eventForm") EventVenueDTO form,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {

            model.addAttribute("events", service.getAll(0, 5));
            model.addAttribute("venues", venueService.findAllVenues());

            return "events";
        }

        // CREAR VENUE
        Venue venue = new Venue();
        venue.setName(form.venueName());
        venue.setCity(form.city());

        venueService.createVenue(venue);

        // CREAR EVENT
        Event event = new Event();
        event.setName(form.eventName());
        event.setDate(form.eventDate());
        event.setVenue(venue);

        service.saveEvent(event);

        return "redirect:/admin/events";
    }
}
