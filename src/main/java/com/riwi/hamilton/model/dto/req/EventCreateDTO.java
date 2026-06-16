package com.riwi.hamilton.model.dto.req;

import com.riwi.hamilton.validation.ValidNameEvent;
import com.riwi.hamilton.validation.groups.CreateGroup;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record EventCreateDTO(
        @Schema(
                description = "Identificador del evento. No debe enviarse durante la creación.",
                example = "null",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        @Null(groups = CreateGroup.class, message = "DTO de creacion no lleva id"
        )
        Long id,

        @Schema(
                description = "Nombre del evento. Debe iniciar con el prefijo EVENT-",
                example = "EVENT-Hamilton Festival",
                minLength = 2,
                maxLength = 255
        )
        @NotBlank(message = "titulo del evento no puede estar vacio")
        @Size(min = 2, max = 255, message = "El titulo debe tener entre 2 y 255 caracteres")
        @ValidNameEvent(message = "El nombre debe de empezar con 'EVENT-'.")
        String name,

        @Schema(
                description = "Fecha del evento. Debe ser hoy o una fecha futura",
                example = "2026-12-24",
                type = "string",
                format = "date"
        )
        @NotNull(message = "La fecha de publicacion es obligatoria")
        @FutureOrPresent(message = "La publicacion del evento no puede tener fecha pasada.")
        LocalDate date,

        @Schema(
                description = "Descripción opcional del evento",
                example = "Evento principal de música y cultura"
        )
        String description,

        @Schema(
                description = "Identificador del lugar donde se realizará el evento",
                example = "10",
                minimum = "1"
        )
        @NotNull(message = "Debe especificar el ID del lugar")
        @Positive(message = "ID del lugar invalido")
        Long venueId,

        @ArraySchema(
                schema = @Schema(
                        description = "Lista de identificadores de categorías asociadas al evento",
                        example = "1",
                        minimum = "1"
                )
        )
        @NotEmpty(message = "El libro debe pertenecer al menos a un género")
        Set<@NotNull(message = "No pueden haber ids nulos") @Positive(message = "ID del categoria invalido") Long> categoriesId
) {
}
