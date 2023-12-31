package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameOrDescriptionContainingIgnoreCaseOrderById(String name, String description);

    List<Item> findByOwner_Id(long id);
}
