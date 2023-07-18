package ru.practicum.categories.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesController {
    private final AdminCategoriesService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/categories")
    public CategoryDto createCategories(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.debug("POST: admin/categories");
        return service.createCategories(newCategoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/categories/{categoryId}")
    public void deleteCategories(@PathVariable(name = "categoryId") Long categoryId) {
        log.debug("DELETE: admin/categories/{}", categoryId);
        service.deleteCategories(categoryId);
    }

    @PatchMapping(path = "/categories/{categoryId}")
    public CategoryDto changeCategories(@PathVariable(name = "categoryId") Long categoryId,
                                        @RequestBody @Valid CategoryDto categoryDto) {
        log.debug("PATCH: admin/categories/{}", categoryId);
        return service.changeCategories(categoryId, categoryDto);
    }
}
