package ru.practicum.explore.model.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoOut {
    private Long id;
    private String text;
    private String publishedOn;
    private String initiatorName;
}
