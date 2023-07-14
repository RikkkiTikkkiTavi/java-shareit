package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class Item {
    private long id;
    private User owner;
    private String name;
    private String description;
    private Boolean available;
}
