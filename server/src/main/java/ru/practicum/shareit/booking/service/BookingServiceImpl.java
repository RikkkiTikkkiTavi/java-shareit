package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingStatusException;
import ru.practicum.shareit.booking.exception.UnsupportedStateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingResponseDto getBooking(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new UserNotFoundException("Вы не автор бронирования и не владелец вещи");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    @Transactional
    public BookingResponseDto addBooking(long userId, BookingRequestDto bookingRequestDto) {
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Предмет не найден"));
        if (!item.getAvailable()) {
            throw new ItemAvailableException("Предмет не доступен");
        }
        if (item.getOwner().getId() == userId) {
            throw new UserNotFoundException("Вещь не может забронировать её владелец");
        }
        Booking booking = BookingMapper.toBooking(bookingRequestDto, item,
                userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден")),
                Status.WAITING);
        return BookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto approveBooking(long userId, long bookingId, boolean approved) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Бронирование не найдено"));
        if (booking.getItem().getOwner().getId() == userId) {
            if (approved) {
                if (booking.getStatus().equals(Status.APPROVED)) {
                    throw new BookingStatusException("Бронирование уже подтверждено");
                }
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new UserNotFoundException("Пользователь не является владельцем вещи");
        }
        return BookingMapper.toBookingResponseDto(booking);
    }

    public List<BookingResponseDto> getBookingsByBooker(long bookerId, String state, int from, int size) {
        userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        switch (State.valueOf(state)) {
            case ALL:
                long count = bookingRepository.countByBooker_Id(bookerId);
                if (size > count - from) {
                    size = (int) (count - from);
                }
                PageRequest pageRequest = PageRequest.of(from, size);
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdOrderByStartDesc(bookerId, pageRequest));
            case CURRENT:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId, LocalDateTime.now(),
                                LocalDateTime.now()));
            case PAST:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(bookerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now()));
            case WAITING:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdAndStatusEquals(bookerId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByBooker_IdAndStatusEquals(bookerId, Status.REJECTED));
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
    }

    public List<BookingResponseDto> getBookingsByOwner(long ownerId, String state, int from, int size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        switch (State.valueOf(state)) {
            case ALL:
                long count = bookingRepository.countByItem_Owner_Id(ownerId);
                if (size > count - from) {
                    size = (int) (count - from);
                }
                PageRequest pageRequest = PageRequest.of(from, size);
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdOrderByStartDesc(ownerId, pageRequest));
            case CURRENT:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId,
                                LocalDateTime.now(), LocalDateTime.now()));
            case PAST:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now()));
            case FUTURE:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now()));
            case WAITING:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdAndStatusEquals(ownerId, Status.WAITING));
            case REJECTED:
                return BookingMapper.toBookingsResponseDto(bookingRepository
                        .findAllByItem_Owner_IdAndStatusEquals(ownerId, Status.REJECTED));
            default:
                throw new UnsupportedStateException("Unknown state: " + state);
        }
    }
}
