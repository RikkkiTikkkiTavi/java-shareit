package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    User user;
    UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1, "user", "email@email");
        userDto = new UserDto(1, "user", "email@email");
    }


    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        assertEquals(List.of(userDto), userService.findAll());
    }

    @Test
    void getUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        assertEquals(userDto, userService.getUser(1));
    }

    @Test
    void create() {
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userDto, userService.create(userDto));
    }

    @Test
    void update() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userDto, userService.update(userDto, 1));
    }
}