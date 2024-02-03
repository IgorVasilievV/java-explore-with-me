package ru.practicum.explore.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.dto.CommentDtoIn;
import ru.practicum.explore.model.comment.dto.CommentDtoOut;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.storage.comment.CommentStorage;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {

    private final CommentStorage commentStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final ValidationCommentService validationCommentService;

    @Transactional
    public CommentDtoOut addComment(Long userId, Long eventId, CommentDtoIn commentDtoIn) {
        validationCommentService.validateBeforeAdded(userId, eventId);

        Event event = eventStorage.findById(eventId).get();
        User initiator = userStorage.findById(userId).get();
        LocalDateTime publishedOn = LocalDateTime.now();

        Comment commentToDb = CommentMapper.toComment(commentDtoIn, event, initiator, publishedOn);
        Comment comment = commentStorage.save(commentToDb);

        log.info("added comment with id=" + comment.getId());
        return CommentMapper.toCommentDtoOut(comment);
    }

    @Transactional
    public CommentDtoOut updateComment(Long userId, Long eventId, Long commentId, CommentDtoIn commentDtoIn) {
        validationCommentService.validateBeforeUpdated(userId, eventId, commentId);

        Comment commentToUpdated = commentStorage.findById(commentId).get();
        LocalDateTime publishedOn = LocalDateTime.now();

        commentToUpdated.setPublishedOn(LocalDateTime.now());
        commentToUpdated.setText(commentDtoIn.getText());
        Comment comment = commentStorage.save(commentToUpdated);

        log.info("updated comment with id=" + commentId);
        return CommentMapper.toCommentDtoOut(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        validationCommentService.validateBeforeDeleted(userId, eventId, commentId);

        commentStorage.deleteById(commentId);

        log.info("deleted comment with id=" + commentId);
    }

    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        validationCommentService.validateCommentId(commentId);

        commentStorage.deleteById(commentId);

        log.info("deleted comment with id=" + commentId);
    }

    public CommentDtoOut getCommentById(Long userId, Long eventId, Long commentId) {
        validationCommentService.validateBeforeGetByCommentId(userId, eventId, commentId);

        Comment comment = commentStorage.findById(commentId).get();

        log.info("got comment with id=" + commentId);
        return CommentMapper.toCommentDtoOut(comment);
    }

    public List<CommentDtoOut> getCommentsByEventId(Long userId, Long eventId) {
        validationCommentService.validateBeforeGetByEventId(userId, eventId);

        List<Sort.Order> orders = new ArrayList<>();
        Sort.Order orderPublishOn = new Sort.Order(Sort.Direction.DESC, "publishedOn");
        Sort.Order orderId = new Sort.Order(Sort.Direction.ASC, "id");
        orders.add(orderPublishOn);
        orders.add(orderId);

        List<Comment> comments = commentStorage.findAllByEventId(eventId, Sort.by(orders));

        log.info("got comments by event with id=" + eventId);
        return comments.stream().map(CommentMapper::toCommentDtoOut).collect(Collectors.toList());
    }
}
