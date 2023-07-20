package ru.practicum.comments.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.model.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .authorName(comment.getAuthor().getName())
                .posted(comment.getCreated())
                .build();
    }
}
