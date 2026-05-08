package com.riwi.hamilton.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long id;
    @NotBlank(message = "Name can´t to be empty.")
    @Size(min = 4, max = 30, message = "The name can´t be shorter than 4 or greater than 30.")
    private String name;
    @NotBlank(message = "Date can´t be empty")
    private String date;
}
