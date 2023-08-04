package ru.practicum.comments.priv;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;

import java.util.List;

public interface PrivateCommentsService {

    CommentDto createComment(NewCommentDto comment, Long eventId, Long userId);

    List<CommentDto> getAllCommentsByUser(Long userId, int from, int size);

    CommentDto getCommentById(Long commentId, Long userId);

    @Transactional
    UpdatedCommentDto updateComment(NewCommentDto comment, Long userId, Long commentId);

    @Transactional
    void deleteComment(Long commentId, Long userId);
}
