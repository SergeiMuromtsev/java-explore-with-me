package ru.practicum.comments.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

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

    public static Comment toCommentWhenUpdating(NewCommentDto comment, Comment old) {
        return Comment.builder()
                .id(old.getId())
                .text(comment.getText())
                .event(old.getEvent())
                .author(old.getAuthor())
                .created(old.getCreated())
                .build();
    }

    public static Comment toCommentFromNewCommentDto(NewCommentDto comment, User author, Event event) {
        return Comment.builder()
                .text(comment.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    public static UpdatedCommentDto toUpdatedCommentDto(Comment comment, String oldText) {
        return UpdatedCommentDto.builder()
                .id(comment.getId())
                .updatedText(comment.getText())
                .text(oldText)
                .eventId(comment.getEvent().getId())
                .authorName(comment.getAuthor().getName())
                .posted(comment.getCreated())
                .build();
    }
}
