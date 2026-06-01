package com.riwi.hamilton.repository;

import com.riwi.hamilton.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
