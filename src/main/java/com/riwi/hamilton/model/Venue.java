package com.riwi.hamilton.model;

import com.riwi.hamilton.utils.Cities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SoftDelete(
        columnName = "available",
        strategy = SoftDeleteType.ACTIVE)
@Table(name = "Venues")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            mappedBy = "venue",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Event> events = new ArrayList<>();

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Name can´t to be empty.")
    @Size(min = 4, max = 30, message = "The name can´t be shorter than 4 or greater than 30.")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 150)
    @NotNull(message = "City can´t to be empty.")
    private Cities city;
}
