package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User booker;
    private User secondBooker;
    private Item itemOne;
    private Item itemTwo;
    private Booking bookingOne;
    private Booking bookingTwo;
    private Booking bookingThree;

    LocalDateTime past;
    LocalDateTime future;
    LocalDateTime now;

    @BeforeAll
    void setUp() {
        User owner = new User(0, "userOne", "email@One.com");
        userRepository.save(owner);

        User secondOwner = new User(0, "userThree", "email@Three.com");
        userRepository.save(secondOwner);

        booker = new User(0, "userTwo", "email@Two.com");
        userRepository.save(booker);

        secondBooker = new User(0, "userFour", "email@Four.com");
        userRepository.save(secondBooker);

        itemOne = new Item(0, owner, "Item1", "Desc1", true, null);
        itemRepository.save(itemOne);

        itemTwo = new Item(0, secondOwner, "Item2", "Desc2", true, null);
        itemRepository.save(itemTwo);

        bookingOne = new Booking(0, LocalDateTime.of(2000, 1, 1, 1, 1, 1),
                LocalDateTime.of(2000, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);
        bookingRepository.save(bookingOne);

        bookingTwo = new Booking(0, LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                LocalDateTime.of(2001, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);
        bookingRepository.save(bookingTwo);

        bookingThree = new Booking(0, LocalDateTime.of(2002, 1, 1, 1, 1, 1),
                LocalDateTime.of(2002, 1, 1, 1, 1, 10), itemTwo, secondBooker,
                Status.WAITING);
        bookingRepository.save(bookingThree);

        past = LocalDateTime.of(1990, 1, 1, 1, 1, 1);
        future = LocalDateTime.of(2010, 1, 1, 1, 1, 1);
        now = LocalDateTime.of(2001, 1, 1, 1, 1, 5);
    }

    @Test
    void countByItem_Owner_Id() {
        assertEquals(2, bookingRepository.countByItem_Owner_Id(1));
    }

    @Test
    void countByBooker_Id() {
        assertEquals(2, bookingRepository.countByBooker_Id(3));
    }

    @Test
    void findAllByBooker_IdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(3,
                PageRequest.of(0, 10));
        assertEquals(2, bookings.size());
        assertEquals(bookingOne, bookings.get(1));
        assertEquals(bookingTwo, bookings.get(0));
        List<Booking> emptyBookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(2,
                PageRequest.of(0, 10));
        assertEquals(0, emptyBookings.size());
    }

    @Test
    void findAllByItem_Owner_IdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(1,
                PageRequest.of(0, 10));
        assertEquals(2, bookings.size());
        assertEquals(bookingOne, bookings.get(1));
        assertEquals(bookingTwo, bookings.get(0));
        List<Booking> emptyBookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(3,
                PageRequest.of(0, 10));
        assertEquals(0, emptyBookings.size());
        List<Booking> bookingOneElement = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(2,
                PageRequest.of(0, 10));
        assertEquals(1, bookingOneElement.size());
        assertEquals(bookingThree, bookingOneElement.get(0));
    }

    @Test
    void findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(3, now, now);
        assertEquals(1, bookings.size());
        List<Booking> emptyBookings = bookingRepository
                .findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(3, LocalDateTime.now(), future);
        assertEquals(0, emptyBookings.size());
    }

    @Test
    void findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(1, now, now);
        assertEquals(1, bookings.size());
        assertEquals(bookingTwo, bookings.get(0));
        List<Booking> emptyBookings = bookingRepository
                .findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(3, LocalDateTime.now(),
                        future);
        assertEquals(0, emptyBookings.size());
    }

    @Test
    void findAllByBooker_IdAndEndIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(3, now);
        assertEquals(1, bookings.size());
        assertEquals(bookingOne, bookings.get(0));
    }

    @Test
    void findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(1, now);
        assertEquals(1, bookings.size());
        assertEquals(bookingOne, bookings.get(0));
    }

    @Test
    void findAllByBooker_IdAndStartIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(4, now);
        assertEquals(1, bookings.size());
        assertEquals(bookingThree, bookings.get(0));
    }

    @Test
    void findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(2, now);
        assertEquals(1, bookings.size());
        assertEquals(bookingThree, bookings.get(0));
    }

    @Test
    void findAllByBooker_IdAndStatusEquals() {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdAndStatusEquals(3, Status.WAITING);
        assertEquals(2, bookings.size());
        assertEquals(Status.WAITING, bookings.get(0).getStatus());
    }

    @Test
    void findAllByItem_Owner_IdAndStatusEquals() {
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndStatusEquals(1, Status.WAITING);
        assertEquals(2, bookings.size());
        assertEquals(Status.WAITING, bookings.get(1).getStatus());
    }

    @Test
    void findAllByItemAndStartIsBeforeAndStatusEqualsOrderByStart() {
        List<Booking> bookings = bookingRepository
                .findAllByItemAndStartIsBeforeAndStatusEqualsOrderByStart(itemOne, now, Status.WAITING);
        assertEquals(2, bookings.size());
        assertEquals(itemOne, bookings.get(1).getItem());
        assertEquals(itemOne, bookings.get(0).getItem());
    }

    @Test
    void findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart() {
        List<Booking> emptyBookings = bookingRepository
                .findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart(itemOne, now, Status.WAITING);
        assertEquals(0, emptyBookings.size());
        List<Booking> bookings = bookingRepository
                .findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart(itemTwo, past, Status.WAITING);
        assertEquals(1, bookings.size());
        assertEquals(itemTwo, bookings.get(0).getItem());
    }

    @Test
    void findFirstByItemAndBooker() {
        Booking bookingFirst = bookingRepository.findFirstByItemAndBooker(itemOne, booker);
        assertEquals(bookingOne, bookingFirst);
        Booking bookingSecond = bookingRepository.findFirstByItemAndBooker(itemTwo, secondBooker);
        assertEquals(bookingThree, bookingSecond);
    }

}