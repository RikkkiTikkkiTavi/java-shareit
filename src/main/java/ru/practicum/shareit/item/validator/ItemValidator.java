package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public class ItemValidator {

    public static void checkOwner(Item item, long ownerId) {
        if (item.getOwnerId() != ownerId) {
            throw new ItemNotFoundException("Редактировать данные вещи может только ее владелец");
        }
    }

    public static void checkItem(Item item) {
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
}
