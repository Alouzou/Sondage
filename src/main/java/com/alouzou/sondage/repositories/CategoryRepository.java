package com.alouzou.sondage.repositories;

import com.alouzou.sondage.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Optional<List<Category>> findAllByIsActiveTrue();
    Optional<Category> findByIdAndIsActiveTrue(Long id);
}