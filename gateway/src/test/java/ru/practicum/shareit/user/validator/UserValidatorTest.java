package ru.practicum.shareit.user.validator;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserValidationException;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    @Test
    void checkEmail() {
        UserDto userDto = new UserDto(1, "name", "email");
        assertThrows(UserValidationException.class, () -> UserValidator.checkEmail(userDto));
    }
}