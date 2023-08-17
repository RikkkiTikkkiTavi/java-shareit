package ru.practicum.shareit.item.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemValidatorTest {

    private Item item;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        User owner = new User(1, "UserOne", "email@email.ru");
        item = new Item(1, owner, "ItemOne", "DescOne", true, null);
        itemDto = new ItemDto(1, "ItemOne", "DescOne", true, 0);
        commentDto = new CommentDto(1, "UserOne", "text", LocalDateTime.of(2000, 1, 1, 1, 1));
        booking = new Booking(2, LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                LocalDateTime.of(2002, 1, 1, 1, 1, 10), item, owner,
                Status.WAITING);
    }

    @Test
    void onlyOwnerCanEditItem() {
        assertThrows(ItemNotFoundException.class, () -> ItemValidator.checkOwner(item, 2));
    }

    @Test
    void itemAvailableMustBeNotNull() {
        itemDto.setAvailable(null);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemNameMustBeNotNull() {
        itemDto.setName(null);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemDescriptionMustBeNotNull() {
        itemDto.setDescription(null);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void commentMustBeNotEmpty() {
        commentDto.setText("");
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkComment(commentDto, booking));
    }

    @Test
    void throwExceptionWhenCommentWithoutBooking() {
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkComment(commentDto, null));
    }

    @Test
    void throwExceptionWhenBookingStatusRejected() {
        booking.setStatus(Status.REJECTED);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkComment(commentDto, booking));
    }

    @Test
    void throwExceptionWhenBookingIsNotEnding() {
        booking.setEnd(LocalDateTime.of(2099, 9, 9, 9, 9));
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkComment(commentDto, booking));
    }
}