package ru.practicum.comments.priv;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;

import java.util.List;

public interface PrivateCommentsService {
    @Transactional
    CommentDto create(NewCommentDto comment, Long eventId, Long userId);

    List<CommentDto> getAllByUser(Long userId, int from, int size);

    CommentDto getById(Long commentId, Long userId);

    @Transactional
    UpdatedCommentDto update(NewCommentDto comment, Long userId, Long commentId);

    @Transactional
    void delete(Long commentId, Long userId);
}
