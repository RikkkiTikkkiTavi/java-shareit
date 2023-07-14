package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserStorage storage;

    @Override
    public List<UserDto> findAll() {
        return storage.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(long id) {
        return UserMapper.toUserDto(storage.getUser(id));
    }

    @Override
    public UserDto create(User user) {
        UserValidator.checkEmail(user);
        UserValidator.checkDuplicateEmail(user, storage.getAll());
        return UserMapper.toUserDto(storage.createUser(user));
    }

    @Override
    public UserDto update(User user, long id) {
        User userOld = storage.getUser(id);
        if (user.getName() != null) {
            userOld.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (!user.getEmail().equals(userOld.getEmail())) {
                UserValidator.checkDuplicateEmail(user, storage.getAll());
            }
            userOld.setEmail(user.getEmail());
        }

        return UserMapper.toUserDto(storage.updateUser(userOld));
    }

    @Override
    public UserDto remove(long id) {
        return UserMapper.toUserDto(storage.removeUser(id));
    }
}
