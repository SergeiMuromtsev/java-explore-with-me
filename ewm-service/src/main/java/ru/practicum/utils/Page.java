package ru.practicum.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.exceptions.ConflictException;

public class Page {
    public static Pageable paged(Integer from, Integer size) {
        Pageable page;
        if (from != null && size != null) {
            if (from < 0 || size < 0) {
                throw new RuntimeException();
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size);
        } else {
            page = PageRequest.of(0, 4);
        }
        return page;
    }

    public static void checkPaging(Integer from, Integer size) {
        if ((from == 0 && size == 0) || (from < 0 && size > 0) ||
                (from == 0 && size < 0)) {
            throw new ConflictException("paging error");
        }
    }
}