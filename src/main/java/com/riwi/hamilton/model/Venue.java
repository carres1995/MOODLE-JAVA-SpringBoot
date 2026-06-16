package com.riwi.hamilton.model;

import com.riwi.hamilton.utils.Cities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
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
@SQLRestriction("available = true") //no mostrar datos desactivados
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

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 150)
    private Cities city;
}
