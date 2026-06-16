package com.riwi.hamilton.model.dto.req;

import com.riwi.hamilton.validation.groups.UpdateGroup;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record EventUpdateDTO(
        @Size(min = 2, max = 255, message = "El titulo debe tener entre 2 y 255 caracteres")
        String name,

        @PastOrPresent(message = "La publicacion del evento no puede tener fecha futura.")
        LocalDate date,

        String Description,

        @Positive(message = "ID del lugar invalido")
        Long venueId,

        Set<@Positive(message = "ID del categoria invalido") Long> categoriesId
) {

}
