package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private UserService service;

    @GetMapping
    public List<UserDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") int id) {
        return service.getUser(id);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable("userId") int id, @RequestBody UserDto user) {
        return service.update(user, id);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int id) {
        service.remove(id);
    }
}
