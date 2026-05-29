package com.riwi.hamilton.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SoftDelete(
        columnName = "available",
        strategy = SoftDeleteType.ACTIVE)
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Name can´t to be empty.")
    @Size(min = 4, max = 30, message = "The name can´t be shorter than 4 or greater than 30.")
    private String name;

    @ManyToOne
    @NotNull(message = "Id Venue can´t be empty")
    @JoinColumn(name = "id_venue")
    private Venue venue;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Date can´t be empty")
    private String date;
}
