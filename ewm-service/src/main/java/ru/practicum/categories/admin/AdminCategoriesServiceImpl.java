package ru.practicum.categories.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoryDto createCategories(NewCategoryDto newCategoryDto) {
        return toCategoryDto(categoriesRepository.save(toCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategories(Long categoryId) {
        categoriesRepository.findById(categoryId).orElseThrow(() -> new NotFoundException(String
                .format("Object id {} not found", categoryId)));
        categoriesRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto changeCategories(Long categoryId, CategoryDto categoryDto) {
        Category oldCategory = categoriesRepository.findById(categoryId).orElseThrow();
        if (categoryDto.getName() != null) {
            oldCategory.setName(categoryDto.getName());
        }
        return toCategoryDto(categoriesRepository.save(oldCategory));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoriesById(Long categoryId) {
        return categoriesRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
    }
}