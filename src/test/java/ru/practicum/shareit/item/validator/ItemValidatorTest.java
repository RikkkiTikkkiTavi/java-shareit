package ru.practicum.shareit.item.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemValidatorTest {

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        User owner = new User(1, "UserOne", "email@email.ru");
        Item item = new Item(1, owner, "ItemOne", "DescOne", true, null);
        Booking booking = new Booking(2, LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                LocalDateTime.of(2002, 1, 1, 1, 1, 10), item, owner,
                Status.WAITING);
        CommentDto commentDto = new CommentDto(1, "name", "text", LocalDateTime.now());
    }

    @Test
    void itemAvailableMustBeNotNull() {
        itemDto = new ItemDto(1, "ItemOne", "DescOne", null, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemNameMustBeNotNull() {
        itemDto = new ItemDto(1, null, "DescOne", true, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }

    @Test
    void itemDescriptionMustBeNotNull() {
        itemDto = new ItemDto(1, "ItemOne", null, true, 0);
        assertThrows(ItemValidationException.class, () -> ItemValidator.checkItem(itemDto));
    }
}