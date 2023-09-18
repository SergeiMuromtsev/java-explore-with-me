package ru.practicum.categories.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;

import static ru.practicum.categories.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.categories.mapper.CategoryMapper.toListCategoryDto;
import static ru.practicum.utils.Page.paged;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicCategoriesServiceImpl implements PublicCategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = paged(from, size);
        return toListCategoryDto(categoriesRepository.findAll(page));
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoriesById(Long categoryId) {
        return toCategoryDto(categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found")));
    }

    @Transactional(readOnly = true)
    @Override
    public Category getCategoryById(Long categoryId) {
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
}
