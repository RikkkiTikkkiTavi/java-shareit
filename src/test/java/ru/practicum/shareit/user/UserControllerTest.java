package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.booking.exception.UnsupportedStateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.when;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1, "name", "email@email.ru");
    }

    @Test
    void findAll() throws Exception {
        when(userService.findAll()).thenReturn(List.of(userDto));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", userDto));
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUser(anyLong())).thenReturn(userDto);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(match("$", userDto));
    }

    @Test
    void create() throws Exception {
        when(userService.create(any(UserDto.class))).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(match("$", userDto));
    }

    @Test
    void update() throws Exception {
        when(userService.update(any(UserDto.class), anyLong())).thenReturn(userDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(match("$", userDto));
    }

    @Test
    void throwDuplicateEmailExceptionGetStatus409() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(DuplicateEmailException.class);

        mvc.perform(get("/users/1"))
                .andExpect(status().isConflict());
    }

    @Test
    void throwUserNotFoundExceptionGetStatus404() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(UserNotFoundException.class);

        mvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    private static ResultMatcher match(String prefix, UserDto userDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(userDto.getId()),
                jsonPath(prefix + ".name").value(userDto.getName()),
                jsonPath(prefix + ".email").value(userDto.getEmail()));
    }
}