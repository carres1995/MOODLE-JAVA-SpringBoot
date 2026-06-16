package com.riwi.hamilton.controller.ui;

import com.riwi.hamilton.exception.BusinessRuleViolationException;
import com.riwi.hamilton.exception.DuplicateResourceException;
import com.riwi.hamilton.exception.ResourceNotFoundException;
import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventUIController {

    private final EventService service;
    private final VenueService venueService;
    private final CategoryService categoryService;

    @GetMapping
    public String showEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model
    ) {
        Slice<EventResDTO> events;

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


        if (!model.containsAttribute("eventForm")) {
            model.addAttribute("eventForm", new EventCreateDTO(null, "", null, "", null, Set.of()));
        }

        loadModelAttributes(model, events, page, size, city, category, startDate, endDate);
        return "events";
    }

    @PostMapping("/save")
    public String saveEvent(
            @Valid @ModelAttribute("eventForm") EventCreateDTO form,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes // <-- Inyectamos RedirectAttributes para los flashes
    ) {
        // CASO 1: Errores de anotaciones de validación (@NotBlank, @NotNull, etc.)
        if (result.hasErrors()) {
            loadModelAttributes(model, service.getAll(0), 0, 5, null, null, null, null);
            model.addAttribute("openEventForm", true);
            // Retornamos la vista directa sin redirección para conservar el BindingResult en Thymeleaf
            return "events";
        }

        try {
            // Intentamos guardar en la base de datos
            service.saveEvent(form);

            // CASO 2: Todo salió perfecto
            redirectAttributes.addFlashAttribute("successMessage", "¡Evento creado exitosamente!");
            return "redirect:/admin/events";

        } catch (ResourceNotFoundException | DuplicateResourceException | BusinessRuleViolationException e) {
            // CASO 3: Falló una validación del negocio en tu Service
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            // Pasamos el formulario actual y la orden de abrir el modal como flash attributes
            // para que no se pierdan los datos al redireccionar
            redirectAttributes.addFlashAttribute("eventForm", form);
            redirectAttributes.addFlashAttribute("openEventForm", true);

            return "redirect:/admin/events";

        } catch (Exception e) {
            // CASO 4: Error inesperado (Base de datos caída, etc.)
            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error inesperado en el servidor.");
            return "redirect:/admin/events";
        }
    }

    // Limpié el método eliminando el parámetro EventCreateDTO form para que sea más modular
    private void loadModelAttributes(Model model, Slice<EventResDTO> events, int page, int size, String city, String category, String startDate, String endDate) {
        model.addAttribute("events", events);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("city", city);
        model.addAttribute("category", category);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("venues", venueService.findAllVenues());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("cities", Cities.values());

        // Si no viene pre-configurado como true desde un error de validación, por defecto es false
        if (!model.containsAttribute("openEventForm")) {
            model.addAttribute("openEventForm", false);
        }
    }
}