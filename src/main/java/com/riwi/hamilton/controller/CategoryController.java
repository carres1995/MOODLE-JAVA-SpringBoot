package com.riwi.hamilton.controller;

import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(description = "CRUD about all logic categories", name = "Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/")
    @Operation(summary = "Save category", description = "Returns 201 Created and the registered event")
    public ResponseEntity<Category> saveCategory(Category category){
        service.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the Id enter", description = "Return the category entered by id or 404 Not Found")
    public ResponseEntity<Category> getById(Long id){
        return service.findCategoryById(id).map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Soft-delete a category", description = "Soft-deletes a category by marking it unavailable. Soft-deleted categories are excluded from query results.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category successfully soft-deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        if (service.softDelete(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/update/{id}")
    @Operation(summary = "Update a category", description = "Return the updated category or 404 Not Found")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, Category category){
        final Category updated = service.updateCategory(id,category);
        if (null == updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }
}
