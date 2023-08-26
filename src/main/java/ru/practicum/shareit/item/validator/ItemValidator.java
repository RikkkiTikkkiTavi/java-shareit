package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

public class ItemValidator {

    public static void checkOwner(Item item, long ownerId) {
        if (item.getOwner().getId() != ownerId) {
            throw new ItemNotFoundException("Редактировать данные вещи может только ее владелец");
        }
    }

    public static void checkItem(ItemDto item) {
        if (item.getAvailable() == null) {
            throw new ItemValidationException("У предметы должен быть статус");
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ItemValidationException("У предмета должно быть имя");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ItemValidationException("У предмета должно описание");
        }
    }

    public static void checkComment(CommentDto commentDto, Booking booking) {
        if (commentDto.getText().isEmpty()) {
            throw new ItemValidationException("Комментарий не может быть пустым");
        }
        if (booking == null) {
            throw new ItemValidationException("Пользователь не арендовал предмет");
        } else if (booking.getStatus().equals(Status.REJECTED)) {
            throw new ItemValidationException("Пользователь не арендовал предмет");
        } else if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ItemValidationException("Пользователь еще не завершил аренду");
        }
    }
}
