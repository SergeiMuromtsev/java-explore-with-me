package ru.practicum.categories.admin;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

public interface AdminCategoriesService {
    CategoryDto createCategories(NewCategoryDto newCategoryDto);

    void deleteCategories(int catId);

    CategoryDto changeCategories(int catId, CategoryDto categoryDto);

    Category findCategoriesById(int catId);
}
