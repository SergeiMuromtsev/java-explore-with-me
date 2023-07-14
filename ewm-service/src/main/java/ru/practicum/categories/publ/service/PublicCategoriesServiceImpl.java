package ru.practicum.categories.publ.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@NoArgsConstructor
@Slf4j
public class PublicCategoriesServiceImpl implements PublicCategoriesService{
    @Autowired
    private CategoriesRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = paged(from, size);
        return toListCategoryDto(repository.findAll(page));
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoriesById(int catId) {
        return toCategoryDto(repository.findById(catId).orElseThrow(() -> new NotFoundException("Category not found")));
    }

    @Transactional(readOnly = true)
    @Override
    public Category getCatById(int catId) {
        return repository.findById(catId).orElseThrow(() -> new NotFoundException("Category not found"));
    }
}
