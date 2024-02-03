package ru.practicum.explore.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.enums.State;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.exception.NotFoundException;
import ru.practicum.explore.model.exception.ValidationException;
import ru.practicum.explore.storage.comment.CommentStorage;
import ru.practicum.explore.storage.event.EventStorage;
import ru.practicum.explore.storage.user.UserStorage;

@Service
@RequiredArgsConstructor
public class ValidateCommentService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CommentStorage commentStorage;

    public void validateBeforeAdd(Long userId, Long eventId) {
        validateUserId(userId);
        validateEventIdBeforeAdd(eventId);
    }

    private void validateEventIdBeforeAdd(Long eventId) {
        validateEventId(eventId);
        Event event = eventStorage.findById(eventId).get();
        if (!State.PUBLISHED.toString().equals(event.getState())) {
            throw new ValidationException("Comment can't be added to didn't published event.");
        }
    }

    private void validateUserId(Long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
    }

    public void validateBeforeUpdate(Long userId, Long eventId, Long commentId) {
        validateEventId(eventId);
        validateCommentId(commentId);
        validateUserId(userId);
        if (userId != commentStorage.findById(commentId).get().getInitiator().getId()) {
            throw new ValidationException("Comment can't be updated by not initiator.");
        }
    }

    private void validateEventId(Long eventId) {
        if (eventStorage.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
    }

    public void validateCommentId(Long commentId) {
        if (commentStorage.findById(commentId).isEmpty()) {
            throw new ValidationException("Comment with id=" + commentId + " not found");
        }
    }

    public void validateBeforeDeleted(Long userId, Long eventId, Long commentId) {
        validateEventId(eventId);
        validateCommentId(commentId);
        validateUserId(userId);
        Long idOwnerComment = commentStorage.findById(commentId).get().getInitiator().getId();
        Long idOwnerEvent = commentStorage.findById(commentId).get().getEvent().getInitiator().getId();
        if (userId != idOwnerComment && userId != idOwnerEvent) {
            throw new ValidationException("Comment can't be deleted by not initiator of comment or event.");
        }
    }

    public void validateGetByCommentId(Long userId, Long eventId, Long commentId) {
        validateUserId(userId);
        validateEventId(eventId);
        validateCommentId(commentId);
    }

    public void validateGetByEventId(Long userId, Long eventId) {
        validateUserId(userId);
        validateEventId(eventId);
    }
}
