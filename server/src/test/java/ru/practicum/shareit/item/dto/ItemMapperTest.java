package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private Item item;

    private Comment comment;

    private ItemDto itemDto;

    private ItemBookingDto itemBookingDto;

    private CommentDto commentDto;

    private ItemRequest request;

    private User owner;

    private User author;

    private BookingResponseDto lastBooking;

    private BookingResponseDto nextBooking;


    @BeforeEach
    void setUp() {
        owner = new User(1, "1", "1");
        author = new User(2, "2", "2");
        UserDto authorDto = new UserDto(2, "2", "2");
        User requestor = new User(3, "2", "2");
        request = new ItemRequest(1, "desc", requestor, LocalDateTime.now().withNano(123456789), new ArrayList<>());
        item = new Item(1, owner, "itemOne", "DescOne", true, request);
        comment = new Comment(1, "text", author, item, LocalDateTime.now().withNano(123456789));
        itemDto = new ItemDto(1, "itemOne", "DescOne", true, 1);
        commentDto = new CommentDto(1, "2", "text", LocalDateTime.now().withNano(123456789));
        lastBooking = new BookingResponseDto(1, 1, LocalDateTime.now().minusHours(1), LocalDateTime.now(),
                itemDto, authorDto, Status.APPROVED);
        nextBooking = new BookingResponseDto(2, 1, LocalDateTime.now().minusHours(1), LocalDateTime.now(),
                itemDto, authorDto, Status.APPROVED);
        itemBookingDto = new ItemBookingDto(1, "itemOne", "DescOne", true, lastBooking, nextBooking,
                List.of(commentDto));
    }

    @Test
    void toItemDto() {
        assertEquals(itemDto, ItemMapper.toItemDto(item));
    }

    @Test
    void toItemsDto() {
        assertEquals(List.of(itemDto), ItemMapper.toItemsDto(List.of(item)));
    }

    @Test
    void toItem() {
        assertEquals(item, ItemMapper.toItem(itemDto, owner, request));
    }

    @Test
    void toItemBookingDto() {
        assertEquals(itemBookingDto, ItemMapper.toItemBookingDto(item, lastBooking, nextBooking, List.of(commentDto)));
    }

    @Test
    void toCommentDto() {
        assertEquals(commentDto, ItemMapper.toCommentDto(comment));
    }

    @Test
    void toComment() {
        assertEquals(comment, ItemMapper.toComment(commentDto, item, author));
    }
}