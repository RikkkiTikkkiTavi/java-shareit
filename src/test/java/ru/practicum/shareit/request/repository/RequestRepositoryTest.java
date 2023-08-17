package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ItemRepository itemRepository;

    private ItemRequest itemRequest;

    private ItemRequest itemRequestTwo;

    @BeforeAll
    void setUp() {
        User user = new User(1, "name", "email");
        userRepository.save(user);
        User userTwo = new User(2, "g", "e");
        userRepository.save(userTwo);
        itemRequest = new ItemRequest(1, "desc", user, LocalDateTime.now().withNano(0));
        itemRequestTwo = new ItemRequest(2, "desc", userTwo, LocalDateTime.now().withNano(0));
        requestRepository.save(itemRequest);
        requestRepository.save(itemRequestTwo);
    }

    @Test
    void findByRequestor_Id() {
        assertEquals(List.of(itemRequest), requestRepository.findByRequestor_Id(1));
    }

    @Test
    void findByRequestor_IdNot() {
        List<ItemRequest> requests = requestRepository.findByRequestor_IdNot(1, PageRequest.of(0, 1)).toList();
        assertEquals(List.of(itemRequestTwo), requests);
    }
}