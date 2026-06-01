package com.riwi.hamilton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SoftDelete(strategy = SoftDeleteType.ACTIVE,columnName = "available")
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 255)
    private String description;
    @JsonIgnore // este es un mecanismo usado para evitar los loops anidados en events; aquí ignoramos esta relación.
    @ManyToMany(mappedBy = "categories",
            cascade = {
                    CascadeType.MERGE, CascadeType.PERSIST
            }
    )
    private Set<Event> events = new HashSet<>();
}
