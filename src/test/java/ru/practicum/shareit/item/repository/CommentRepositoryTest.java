package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    Item item;
    Comment commentOne;

    Comment commentTwo;

    @BeforeAll
    void setUp() {
        User owner = new User(1, "", "");
        userRepository.save(owner);
        item = new Item(1, owner, "name", "desc", true, null);
        itemRepository.save(item);
        User author = new User(2, "name", "email");
        userRepository.save(author);
        commentOne = new Comment(1, "text", author, item, LocalDateTime.of(2010, 1, 1, 1, 1));
        commentRepository.save(commentOne);
        commentTwo = new Comment(2, "text", author, item, LocalDateTime.of(2010, 1, 1, 1, 5));
        commentRepository.save(commentTwo);
    }

    @Test
    void findAllByItem() {
        assertEquals(List.of(commentOne, commentTwo), commentRepository.findAllByItem(item));
    }
}