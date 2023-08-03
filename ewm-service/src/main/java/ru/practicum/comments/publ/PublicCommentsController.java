package ru.practicum.comments.publ;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comments.dto.CommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCommentsController {
    private final PublicCommentsService publicCommentsService;

    @GetMapping
    public List<CommentDto> getAllByEvent(@RequestParam Long eventId,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                          @RequestParam(defaultValue = "10") @Positive int size) {
        log.debug("GET: all comments for event {}", eventId);
        return publicCommentsService.getAllByEvent(eventId, from, size);
    }
}
