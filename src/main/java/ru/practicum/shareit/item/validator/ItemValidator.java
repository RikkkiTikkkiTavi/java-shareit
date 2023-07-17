package ru.practicum.shareit.item.validator;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemValidator {

    public static void checkOwner(Item item, long ownerId) {
        if (item.getOwner().getId() != ownerId) {
            throw new ItemNotFoundException("Редактировать данные вещи может только ее владелец");
        }
    }

    public static void checkExistenceItem(Item item) {
        if (item == null) {
            throw new ItemNotFoundException("Вещь c данным id не зарегистрирована");
        }
    }

    public static void checkExistenceUser(User user) {
        if (user == null) {
            throw new ItemNotFoundException("Пользователь с данным id не зарегистрирован");
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
