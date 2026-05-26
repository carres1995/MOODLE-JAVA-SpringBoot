package com.riwi.hamilton.controller.ui;

import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.model.dto.VenueForm;
import com.riwi.hamilton.service.VenueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/venues")
@RequiredArgsConstructor
public class VenueUIController {

    private final VenueService service;

    @GetMapping
    public String showVenues(Model model) {
        model.addAttribute("venues", service.findAllVenues());
        model.addAttribute("venueForm", new VenueForm());
        return "venues";
    }

    @PostMapping("/save")
    public String saveVenue(@Valid @ModelAttribute("venueForm") VenueForm venueForm,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("venues", service.findAllVenues());
            return "venues";
        }

        Venue venue = new Venue();
        venue.setName(venueForm.getName());
        venue.setCity(venueForm.getCity());
        service.createVenue(venue);
        return "redirect:/admin/venues";
    }
}
