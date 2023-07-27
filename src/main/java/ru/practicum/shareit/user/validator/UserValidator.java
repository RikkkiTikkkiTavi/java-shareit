package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public class UserValidator {

    public static void checkEmail(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new UserValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    public static void checkExistenceUser(Optional<User> opt) {
        if (opt.isEmpty()) {
            throw new ItemNotFoundException("Пользователь с данным id не зарегистрирован");
        }
    }
}
