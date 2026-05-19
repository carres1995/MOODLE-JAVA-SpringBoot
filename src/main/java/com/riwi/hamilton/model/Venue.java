package com.riwi.hamilton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Name can´t to be empty.")
    @Size(min = 4, max = 30, message = "The name can´t be shorter than 4 or greater than 30.")
    private String name;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "City can´t to be empty.")
    private String city;
}
