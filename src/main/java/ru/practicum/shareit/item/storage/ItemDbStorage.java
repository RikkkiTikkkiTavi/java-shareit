package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemDbStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private long id = 1;

    @Override
    public Item createItem(Item item) {
        item.setId(id);
        items.put(id, item);
        id++;
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getItem(long id) {
        return items.get(id);
    }
}
