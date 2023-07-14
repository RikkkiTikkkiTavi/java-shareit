package ru.practicum.shareit.user.validator;

import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class UserValidator {

    public static void checkDuplicateEmail(User checkUser, List<User> users) {
        for (User user : users) {
            if (checkUser.getEmail().equals(user.getEmail())) {
                throw new DuplicateEmailException("Пользователь с таким email уже существует");
            }
        }
    }

    public static void checkEmail(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new UserValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
    }
}
