package com.alouzou.sondage.controllers;

import com.alouzou.sondage.entities.Category;
import com.alouzou.sondage.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/categories")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category cat = categoryService.createCategory(category.getName(), category.isActive());
        return ResponseEntity.ok(cat);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/modify/{CategoryId}")
    public ResponseEntity<Category> modifyCategory(
            @PathVariable("CategoryId") Long id,
            @RequestBody Category category){
        Category cat = categoryService.modifyCategory(id, category);
        return ResponseEntity.ok(cat);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{idCategory}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("idCategory") Long id
    ){
        categoryService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Category>> findAllActive(){
        return ResponseEntity.ok(categoryService.findAllByIsActive());
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<Category> findByIdActive(@PathVariable Long id){
        return ResponseEntity
                .ok(categoryService.findByIdAndIsActiveTrue(id));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        return ResponseEntity
                .ok(categoryService.findById(id));
    }
}
