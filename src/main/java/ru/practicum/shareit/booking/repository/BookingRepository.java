package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    int countByItem_Owner_Id(long ownerId);

    int countByBooker_Id(long bookerId);

    List<Booking> findAllByBooker_IdOrderByStartDesc(long bookerId, PageRequest request);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long ownerId, PageRequest request);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long bookerId, LocalDateTime start,
                                                                                  LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId, LocalDateTime start,
                                                                                      LocalDateTime end);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStatusEquals(long bookerId, Status status);

    List<Booking> findAllByItem_Owner_IdAndStatusEquals(long ownerId, Status status);

    LinkedList<Booking> findAllByItemAndStartIsBeforeAndStatusEqualsOrderByStart(Item item, LocalDateTime end,
                                                                                 Status status);

    LinkedList<Booking> findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart(Item item, LocalDateTime end,
                                                                                Status status);

    Booking findFirstByItemAndBooker(Item item, User booker);
}