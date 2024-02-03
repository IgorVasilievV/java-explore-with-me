package ru.practicum.explore.service.comment;

import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.comment.dto.CommentDtoOut;
import ru.practicum.explore.model.comment.dto.CommentDtoIn;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        return CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .publishedOn(comment.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiatorName(comment.getInitiator().getName())
                .build();
    }

    public static Comment toComment(CommentDtoIn commentDtoIn, Event event, User initiator, LocalDateTime publishedOn) {
        return Comment.builder()
                .text(commentDtoIn.getText())
                .event(event)
                .initiator(initiator)
                .publishedOn(publishedOn)
                .build();
    }
}
