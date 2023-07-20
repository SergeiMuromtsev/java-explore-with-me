package ru.practicum.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comments.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAuthorId(Long userId, Pageable page);
}