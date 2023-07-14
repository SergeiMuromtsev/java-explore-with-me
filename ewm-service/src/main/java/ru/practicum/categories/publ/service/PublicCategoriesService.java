package ru.practicum.categories.publ.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoriesById(int catId);

    Category getCatById(int catId);
}
