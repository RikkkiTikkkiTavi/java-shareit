package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static ItemBookingDto toItemBookingDto(Item item, BookingResponseDto lastBooking, BookingResponseDto nextBooking, List<CommentDto> comments) {
        return new ItemBookingDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                lastBooking, nextBooking, comments);
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getAuthor().getName(), comment.getText(), comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return new Comment(0, commentDto.getText(), author, item, LocalDateTime.now());
    }
}
