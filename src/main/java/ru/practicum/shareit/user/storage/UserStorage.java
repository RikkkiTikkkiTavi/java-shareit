package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    List<User> getAll();

    User getUser(long id);

    User removeUser(long id);
}
