package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;

public class ItemValidator {

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

    public static void checkComment(CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ItemValidationException("Комментарий не может быть пустым");
        }
    }
}
