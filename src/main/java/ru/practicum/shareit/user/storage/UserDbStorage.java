package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDbStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User createUser(User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public User removeUser(long id) {
        return users.remove(id);
    }
}
