package com.riwi.hamilton.service;

import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.repository.CategoryRepository;
import com.riwi.hamilton.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;
    private final ValidationService validation;

    public Category saveCategory(Category category){
        validation.ObjectExist(category);
        return repository.save(category);
    }

    public Optional<Category> findCategoryById(Long id){
        validation.idExist(id);
        return repository.findById(id);
    }

    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    public List<Category> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public Category updateCategory(Long id, Category category){
        validation.idExist(id);
        validation.ObjectExist(category);
        return repository.findById(id).map(c -> {
            c.setName(category.getName());
            c.setDescription(category.getDescription());
            c.setEvents(category.getEvents());
            return repository.save(c);
        }).orElseThrow(() -> new RuntimeException("Could not save Category"));
    }

    public Boolean softDelete(Long id){
        validation.idExist(id);
        repository.deleteById(id);
        return true;
    }
}
