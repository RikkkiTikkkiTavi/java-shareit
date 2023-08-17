package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1, "name", "email");
        userDto = new UserDto(1, "name", "email");
    }

    @Test
    void toUserDto() {
        assertEquals(userDto, UserMapper.toUserDto(user));
    }

    @Test
    void toUser() {
        assertEquals(user, UserMapper.toUser(userDto));
    }
}