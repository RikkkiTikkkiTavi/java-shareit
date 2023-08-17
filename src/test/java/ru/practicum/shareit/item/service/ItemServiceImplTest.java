package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private Item itemOne;
    private ItemDto itemDto;
    private ItemDto itemDtoTwo;
    private ItemRequest itemRequest;
    private Booking bookingOne;
    private Booking bookingTwo;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        owner = new User(1, "userOne", "email@One.com");
        User booker = new User(2, "userTwo", "email@Two.com");
        itemRequest = new ItemRequest(1, "desc", booker, LocalDateTime.now().withNano(0));
        itemOne = new Item(1, owner, "Item1", "Desc1", true, itemRequest);
        itemDto = new ItemDto(1, "Item1", "Desc1", true, 1);
        itemDtoTwo = new ItemDto(2, "Item2", "Desc2", true, 0);
        comment = new Comment(1, "text", owner, itemOne, LocalDateTime.now().withNano(0));
        commentDto = ItemMapper.toCommentDto(comment);
        bookingOne = new Booking(1, LocalDateTime.of(2030, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);
        bookingTwo = new Booking(2, LocalDateTime.of(2001, 1, 1, 1, 1, 1),
                LocalDateTime.of(2025, 1, 1, 1, 1, 10), itemOne, booker,
                Status.WAITING);

    }

    @Test
    void addNewItemThrowExceptionIfItemNotExist() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь не найден"));
        assertThrows(UserNotFoundException.class, () -> itemService.addNewItem(5, itemDto));
    }

    @Test
    void addNewItemThrowExceptionIfRequestNotExist() {
        when(requestRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Запроса не существует"));
        itemDto.setRequestId(100);
        assertThrows(ItemNotFoundException.class, () -> itemService.addNewItem(5, itemDto));
    }

    @Test
    void addNewItemReturnItemDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(owner));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.save(any(Item.class))).thenReturn(itemOne);
        assertEquals(itemDto, itemService.addNewItem(1, itemDto));
    }

    @Test
    void editItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemOne));
        itemOne.setName(itemDtoTwo.getName());
        itemOne.setDescription(itemDtoTwo.getDescription());
        itemOne.setAvailable(itemDtoTwo.getAvailable());
        when(itemRepository.save(any(Item.class))).thenReturn(itemOne);
        assertEquals(ItemMapper.toItemDto(itemOne), itemService.editItem(1, 1, itemDtoTwo));
    }

    @Test
    void getItemThrowExceptionIfItemNotExist() {
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Запроса не существует"));
        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(1, 1));
    }

    @Test
    void getItemReturnItemBookingDto() {
        LinkedList<Booking> OneList = new LinkedList<>();
        OneList.add(bookingOne);
        LinkedList<Booking> TwoList = new LinkedList<>();
        TwoList.add(bookingTwo);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemOne));
        when(bookingRepository
                .findAllByItemAndStartIsBeforeAndStatusEqualsOrderByStart(any(Item.class), any(LocalDateTime.class),
                        any(Status.class))).thenReturn(OneList);
        when(bookingRepository
                .findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart(any(Item.class), any(LocalDateTime.class),
                        any(Status.class))).thenReturn(TwoList);
        when(commentRepository.findAllByItem(any(Item.class))).thenReturn(List.of(comment));
        CommentDto commentDto = ItemMapper.toCommentDto(comment);
        ItemBookingDto itemBookingDto = new ItemBookingDto(itemOne.getId(), itemOne.getName(), itemOne.getDescription(),
                itemOne.getAvailable(), BookingMapper.toBookingResponseDto(bookingOne),
                BookingMapper.toBookingResponseDto(bookingTwo), List.of(commentDto));

        assertEquals(itemBookingDto, itemService.getItem(1, 1));

        when(itemRepository.findByOwner_Id(anyLong())).thenReturn(List.of(itemOne));

        assertEquals(List.of(itemBookingDto), itemService.getUserItems(1));
    }

    @Test
    void searchItemsReturnListOfItemDto() {
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(itemOne));
        assertEquals(List.of(itemDto), itemService.searchItems("text"));
        assertEquals(new ArrayList<>(), itemService.searchItems(""));
    }

    @Test
    void addCommentThrowExceptionIfItemNotFound() {
        when(itemRepository.findById(anyLong())).thenThrow(new ItemNotFoundException("Запроса не существует"));
        assertThrows(ItemNotFoundException.class, () -> itemService.addComment(1, 1, commentDto));
    }

    @Test
    void addCommentThrowExceptionIfUserNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemOne));
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь не найден"));
        assertThrows(UserNotFoundException.class, () -> itemService.addComment(1, 1, commentDto));
    }

    @Test
    void addCommentReturnCommentDto() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(itemOne));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        bookingOne.setStart(LocalDateTime.of(2000, 1, 1, 1, 1));
        bookingOne.setEnd(LocalDateTime.of(2001, 1, 1, 1, 1));
        when(bookingRepository.findFirstByItemAndBooker(any(Item.class), any(User.class))).thenReturn(bookingOne);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        assertEquals(commentDto, itemService.addComment(1, 1, commentDto));
    }
}