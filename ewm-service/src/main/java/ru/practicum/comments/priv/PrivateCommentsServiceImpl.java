package ru.practicum.comments.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.dto.UpdatedCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.Page;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PrivateCommentsServiceImpl implements PrivateCommentsService {
    private final UserRepository usersRepository;
    private final EventsRepository eventsRepository;
    private final CommentsRepository commentRepository;

    @Override
    @Transactional
    public CommentDto create(NewCommentDto comment, Long eventId, Long userId) {
        Event event = eventsRepository.findEventsByIdAndStateIs(eventId, EventStatus.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Creating comment: Event not found"));
        User user = getUserById(userId);
        Comment savedComment = commentRepository.save(CommentMapper.toCommentFromNewCommentDto(comment, user, event));
        return CommentMapper.toCommentDto(savedComment);
    }

    @Override
    public List<CommentDto> getAllByUser(Long userId, int from, int size) {
        Pageable page = Page.paged(from, size);
        getUserById(userId);
        return commentRepository.findAllByAuthorId(userId, page)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getById(Long commentId, Long userId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public UpdatedCommentDto update(NewCommentDto comment, Long userId, Long commentId) {
        getUserById(userId);
        Comment old = getCommentById(commentId);
        Comment update = CommentMapper.toCommentWhenUpdating(comment, old);
        return CommentMapper.toUpdatedCommentDto(commentRepository.save(update), old.getText());
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        getUserById(userId);
        getCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    private User getUserById(Long userId) {
        return usersRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found"));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Comment not found"));
    }
}
