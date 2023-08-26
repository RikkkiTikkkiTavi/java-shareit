package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Пользователь с данным id не зарегистрирован"));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        UserValidator.checkEmail(userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    public UserDto update(UserDto user, long id) {
        User userOld = userRepository.findById(id).orElseThrow();
        if (user.getName() != null) {
            userOld.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userOld.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(userOld));
    }

    @Transactional
    public void remove(long id) {
        userRepository.deleteById(id);
    }
}
