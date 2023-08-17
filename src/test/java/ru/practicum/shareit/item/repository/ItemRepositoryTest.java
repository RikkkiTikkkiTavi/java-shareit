package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RequestRepository requestRepository;

    private Item itemOne;
    private Item itemTwo;

    @BeforeAll
    void setUp() {
        User owner = new User(0, "userOne", "email@One.com");
        userRepository.save(owner);

        User secondOwner = new User(0, "userThree", "email@Three.com");
        userRepository.save(secondOwner);

        User booker = new User(0, "userTwo", "email@Two.com");
        userRepository.save(booker);

        ItemRequest itemRequest = new ItemRequest(1, "desc", secondOwner, LocalDateTime.now().withNano(0));
        requestRepository.save(itemRequest);

        itemOne = new Item(0, owner, "Item1", "Desc1", true, itemRequest);
        itemRepository.save(itemOne);

        itemTwo = new Item(0, secondOwner, "Item2", "Desc2", true, null);
        itemRepository.save(itemTwo);
    }


    @Test
    void findByNameOrDescriptionContainingIgnoreCase() {
        assertEquals(List.of(itemOne, itemTwo),
                itemRepository.findByNameOrDescriptionContainingIgnoreCase("Item1", "Desc2"));
    }

    @Test
    void findByOwner_Id() {
        assertEquals(List.of(itemOne), itemRepository.findByOwner_Id(1));
        assertEquals(List.of(itemTwo), itemRepository.findByOwner_Id(2));
    }

    @Test
    void findByItemRequest_Id() {
        assertEquals(List.of(itemOne), itemRepository.findByItemRequest_Id(1));
    }
}