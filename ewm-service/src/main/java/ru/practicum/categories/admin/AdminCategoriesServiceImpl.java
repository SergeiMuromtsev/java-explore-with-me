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
    public void deleteCategories(int catId) {
        categoriesRepository.findById(catId).orElseThrow(() -> new NotFoundException(String
                .format("Object id {} not found", catId)));
        categoriesRepository.deleteById(catId);
    }

    @Override
    public CategoryDto changeCategories(int catId, CategoryDto categoryDto) {
        Category oldCategory = categoriesRepository.findById(catId).orElseThrow();
        if (categoryDto.getName() != null) {
            oldCategory.setName(categoryDto.getName());
        }
        return toCategoryDto(categoriesRepository.save(oldCategory));
    }

    @Transactional(readOnly = true)
    @Override
    public Category findCategoriesById(int catId) {
        return categoriesRepository.findById(catId).orElseThrow(() -> new NotFoundException("Category not found"));
    }
}