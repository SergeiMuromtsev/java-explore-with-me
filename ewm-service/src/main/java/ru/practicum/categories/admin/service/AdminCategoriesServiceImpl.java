package ru.practicum.categories.admin.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.exceptions.NotFoundException;

import static ru.practicum.categories.mapper.CategoryMapper.toCategory;
import static ru.practicum.categories.mapper.CategoryMapper.toCategoryDto;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    @Autowired
    private CategoriesRepository repository;

    @Override
    public CategoryDto createCategories(NewCategoryDto newCategoryDto) {
        return toCategoryDto(repository.save(toCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategories(int catId) {
        repository.findById(catId).orElseThrow(() -> new NotFoundException(String.format("Object id {} not found", catId)));
        repository.deleteById(catId);
    }

    @Override
    public CategoryDto changeCategories(int catId, CategoryDto categoryDto) {
        Category oldCategory = repository.findById(catId).orElseThrow();
        if (categoryDto.getName() != null) {
            oldCategory.setName(categoryDto.getName());
        }
        return toCategoryDto(repository.save(oldCategory));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoriesById(int catId) {
        return repository.findById(catId).orElseThrow(() -> new NotFoundException("Category not found"));
    }
}