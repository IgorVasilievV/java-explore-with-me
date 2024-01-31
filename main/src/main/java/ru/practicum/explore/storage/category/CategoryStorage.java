package ru.practicum.explore.storage.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.category.Category;

public interface CategoryStorage extends JpaRepository<Category, Long> {
}
