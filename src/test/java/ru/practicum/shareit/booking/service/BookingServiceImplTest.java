package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingValidateException;
import ru.practicum.shareit.booking.exception.UnsupportedStateException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item itemOne;
    private Booking bookingOne;
    private Booking bookingTwo;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        owner = new User(1, "userOne", "email@One.com");
        booker = new User(3, "userTwo", "email@Two.com");
        itemOne = new Item(1, owner, "Item1", "Desc1", true, null);
        bookingOne = new Booking(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);
        bookingTwo = new Booking(2, LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                LocalDateTime.of(2025, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);

        bookingRequestDto = new BookingRequestDto(1,
                LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 1, 1, 1, 10));
    }

    @Test
    void getBookingReturnBookingDtoByBookingId() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingOne));

        assertEquals(BookingMapper.toBookingResponseDto(bookingOne), bookingService.getBooking(3, 1));
    }

    @Test
    void getBookingThrowExceptionByNotExistUserOrBooking() {
        when(userRepository.findById(5L)).thenThrow(new UserNotFoundException("Пользователь не найден"));
        when(bookingRepository.findById(4L)).thenThrow(new BookingNotFoundException("Бронирование не найдено"));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingOne));

        assertThrows(UserNotFoundException.class, () -> bookingService.getBooking(5, 1));
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBooking(3, 4));
    }

    @Test
    void addBookingReturnBookingDto() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingOne);

        assertEquals(BookingMapper.toBookingResponseDto(bookingOne),
                bookingService.addBooking(3, bookingRequestDto));
    }

    @Test
    void addBookingThrowExceptionByNotExistUser() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        when(userRepository.findById(5L)).thenThrow(new UserNotFoundException("Юзер не найден"));
        assertThrows(UserNotFoundException.class, () -> bookingService.addBooking(5, bookingRequestDto));
    }

    @Test
    void addBookingThrowExceptionByNotExistItem() {
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Предмет не найден"));
        assertThrows(ItemNotFoundException.class, () -> bookingService.addBooking(5, bookingRequestDto));
    }

    @Test
    void addBookingThrowExceptionByNotAvailableItem() {
        itemOne.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        assertThrows(ItemValidationException.class, () -> bookingService.addBooking(5, bookingRequestDto));
    }

    @Test
    void addBookingThrowExceptionWhenOwnerIsBooker() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemOne));
        assertThrows(UserNotFoundException.class, () -> bookingService.addBooking(1, bookingRequestDto));
    }

    @Test
    void approveBookingReturnBookingDtoWithStatusApprove() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        BookingResponseDto expectedBooking = BookingMapper.toBookingResponseDto(bookingOne);
        expectedBooking.setStatus(Status.REJECTED);
        assertEquals(expectedBooking, bookingService.approveBooking(1, 1, false));
        expectedBooking.setStatus(Status.APPROVED);
        assertEquals(expectedBooking, bookingService.approveBooking(1, 1, true));
    }

    @Test
    void approveBookingThrowExceptionByNotExistUser() {
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> bookingService.approveBooking(99, 1, true));
    }

    @Test
    void approveBookingThrowExceptionByNotExistBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findById(anyLong())).thenThrow(BookingNotFoundException.class);
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.approveBooking(1, 99, true));
    }

    @Test
    void approveBookingThrowExceptionWhenApproveAgain() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        bookingOne.setStatus(Status.APPROVED);
        assertThrows(BookingValidateException.class,
                () -> bookingService.approveBooking(1, 1, true));
    }

    @Test
    void approveBookingThrowExceptionWhenUserNotOwner() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingOne));
        assertThrows(UserNotFoundException.class,
                () -> bookingService.approveBooking(3, 3, true));
    }

    @Test
    void getBookingsByBookerThrowExceptionByNotExistUser() {
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingsByBooker(99, "Approve", 0, 10));
    }

    @Test
    void getBookingsByBookerStateAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.countByBooker_Id(anyLong())).thenReturn(2);
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(3, PageRequest.of(0, 2)))
                .thenReturn(List.of(bookingOne, bookingTwo));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne, bookingTwo)),
                bookingService.getBookingsByBooker(3, "ALL", 0, 10));
    }

    @Test
    void getBookingsByBookerStateCurrent() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                any(), any())).thenReturn(List.of(bookingTwo));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingTwo)),
                bookingService.getBookingsByBooker(3, "CURRENT", 0, 10));
    }

    @Test
    void getBookingsByBookerStatePast() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByBooker(3, "PAST", 0, 10));
    }

    @Test
    void getBookingsByBookerStateFuture() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByBooker(3, "FUTURE", 0, 10));
    }

    @Test
    void getBookingsByBookerStateWaiting() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByBooker_IdAndStatusEquals(3, Status.WAITING))
                .thenReturn(List.of(bookingOne));
        when(bookingRepository.findAllByBooker_IdAndStatusEquals(3, Status.REJECTED))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByBooker(3, "WAITING", 0, 10));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByBooker(3, "REJECTED", 0, 10));
    }

    @Test
    void getBookingByBookerThrowExceptionUnknownState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        assertThrows(UnsupportedStateException.class,
                () -> bookingService.getBookingsByBooker(3, "UNKNOWN", 0, 10));
    }

    @Test
    void getBookingsByOwnerThrowExceptionByNotExistBooker() {
        when(userRepository.findById(anyLong())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,
                () -> bookingService.getBookingsByOwner(99, "Approve", 0, 10));
    }

    @Test
    void getBookingsByOwnerStateAll() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.countByItem_Owner_Id(anyLong())).thenReturn(2);
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(1, PageRequest.of(0, 2)))
                .thenReturn(List.of(bookingOne, bookingTwo));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne, bookingTwo)),
                bookingService.getBookingsByOwner(1, "ALL", 0, 10));
    }

    @Test
    void getBookingsByOwnerStateCurrent() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                any(), any())).thenReturn(List.of(bookingTwo));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingTwo)),
                bookingService.getBookingsByOwner(1, "CURRENT", 0, 10));
    }

    @Test
    void getBookingsByOwnerStatePast() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByItem_Owner_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByOwner(3, "PAST", 0, 10));
    }

    @Test
    void getBookingsByOwnerStateFuture() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByItem_Owner_IdAndStartIsAfterOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByOwner(3, "FUTURE", 0, 10));
    }

    @Test
    void getBookingsByOwnerStateWaiting() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        when(bookingRepository.findAllByItem_Owner_IdAndStatusEquals(3, Status.WAITING))
                .thenReturn(List.of(bookingOne));
        when(bookingRepository.findAllByItem_Owner_IdAndStatusEquals(3, Status.REJECTED))
                .thenReturn(List.of(bookingOne));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByOwner(3, "WAITING", 0, 10));
        assertEquals(BookingMapper.toBookingsResponseDto(List.of(bookingOne)),
                bookingService.getBookingsByOwner(3, "REJECTED", 0, 10));
    }

    @Test
    void getBookingByOwnerThrowExceptionUnknownState() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booker));
        assertThrows(UnsupportedStateException.class,
                () -> bookingService.getBookingsByOwner(3, "UNKNOWN", 0, 10));
    }
}
