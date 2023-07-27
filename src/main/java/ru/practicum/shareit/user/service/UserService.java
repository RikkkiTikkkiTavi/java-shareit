package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto getUser(long id);

    UserDto create(User user);

    UserDto update(User user, long id);

    void remove(long id);
}
