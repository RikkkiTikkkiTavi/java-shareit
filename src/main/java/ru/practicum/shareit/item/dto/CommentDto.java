package ru.practicum.shareit.item.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentDto {
    long id;
    String authorName;
    String text;
    LocalDateTime created;
}
