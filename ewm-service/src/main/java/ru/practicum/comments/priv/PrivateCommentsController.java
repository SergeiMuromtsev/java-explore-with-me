package ru.practicum.comments.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateCommentsController {
    private final PrivateCommentsService privateCommentsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long userId,
                             @RequestParam Long eventId,
                             @RequestBody @Valid NewCommentDto newComment) {
        return privateCommentsService.create(newComment, eventId, userId);
    }

    @GetMapping
    public List<CommentDto> getAllByUser(@PathVariable Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        return privateCommentsService.getAllByUser(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getById(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        return privateCommentsService.getById(commentId, userId);
    }

    @PatchMapping("/{commentId}")
    public UpdatedCommentDto update(@PathVariable Long userId,
                                   @PathVariable Long commentId,
                                   @RequestBody @Valid NewCommentDto comment) {
        return privateCommentsService.update(comment, userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        privateCommentsService.delete(commentId, userId);
    }
}