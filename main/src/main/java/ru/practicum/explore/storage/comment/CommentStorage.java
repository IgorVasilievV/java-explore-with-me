package ru.practicum.explore.storage.comment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.comment.Comment;

import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long eventId, Sort sort);
}
