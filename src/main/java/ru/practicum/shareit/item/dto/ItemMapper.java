package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        long requestId = 0;
        if (item.getItemRequest() != null) {
            requestId = item.getItemRequest().getId();
        }
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), requestId);
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        return new Item(itemDto.getId(), owner, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                itemRequest);
    }

    public static ItemBookingDto toItemBookingDto(Item item, BookingResponseDto lastBooking, BookingResponseDto nextBooking,
                                                  List<CommentDto> comments) {
        return new ItemBookingDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                lastBooking, nextBooking, comments);
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getAuthor().getName(), comment.getText(), comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return new Comment(commentDto.getId(), commentDto.getText(), author, item, LocalDateTime.now());
    }
}
