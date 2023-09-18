package ru.practicum.categories.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicCategoriesController {
    @Autowired
    private PublicCategoriesService service;

    @GetMapping(path = "/categories")
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        log.debug("GET: /categories");
        return service.getCategories(from, size);
    }

    @GetMapping(path = "/categories/{categoryId}")
    public CategoryDto getCategoriesById(@PathVariable(name = "categoryId") Long categoryId) {
        log.debug("GET: /categories/{}", categoryId);
        return service.getCategoriesById(categoryId);
    }

}
