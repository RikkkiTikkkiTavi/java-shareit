package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("Пользователь с данным id не зарегистрирован"));
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != 0) {
            itemRequest = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ItemNotFoundException("Такого запроса не существует"));
        }
        Item item = ItemMapper.toItem(itemDto, owner, itemRequest);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto editItem(long itemId, long userId, ItemDto item) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow();
        if (oldItem.getOwner().getId() != userId) {
            throw new ItemNotFoundException("Редактировать данные вещи может только ее владелец");
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    public ItemBookingDto getItem(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Вещь c данным id не зарегистрирована"));
        return getItemBookingDto(item, userId);
    }

    public List<ItemBookingDto> getUserItems(long userId) {
        return itemRepository.findByOwner_Id(userId)
                .stream()
                .map((item) -> getItemBookingDto(item, userId))
                .sorted(Comparator.comparing(ItemBookingDto::getId))
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseOrderById(text, text)
                .stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Предмет не найден"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователя нет"));
        Booking booking = bookingRepository.findFirstByItemAndBooker(item, user);
        if (commentDto.getText().isEmpty()) {
            throw new ItemValidationException("Комментарий не может быть пустым");
        }
        if (booking == null) {
            throw new ItemValidationException("Пользователь не арендовал предмет");
        } else if (booking.getStatus().equals(Status.REJECTED)) {
            throw new ItemValidationException("Пользователь не арендовал предмет");
        } else if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ItemValidationException("Пользователь еще не завершил аренду");
        }
        return ItemMapper.toCommentDto(commentRepository.save(ItemMapper.toComment(commentDto, item, user)));
    }

    private ItemBookingDto getItemBookingDto(Item item, long userId) {
        BookingResponseDto lastBooking = null;
        BookingResponseDto nextBooking = null;

        LinkedList<Booking> bookingsBefore = bookingRepository
                .findAllByItemAndStartIsBeforeAndStatusEqualsOrderByStart(item, LocalDateTime.now(), Status.APPROVED);
        LinkedList<Booking> bookingsAfter = bookingRepository
                .findAllByItemAndStartIsAfterAndStatusEqualsOrderByStart(item, LocalDateTime.now(), Status.APPROVED);

        if (item.getOwner().getId() == userId) {
            if (bookingsBefore.size() != 0) {
                lastBooking = BookingMapper.toBookingResponseDto(bookingsBefore.getLast());
            }
            if (bookingsAfter.size() != 0) {
                nextBooking = BookingMapper.toBookingResponseDto(bookingsAfter.getFirst());
            }
        }
        List<CommentDto> comments = commentRepository.findAllByItem(item).stream().map(ItemMapper::toCommentDto)
                .collect(Collectors.toList());
        return ItemMapper.toItemBookingDto(item, lastBooking, nextBooking, comments);
    }
}
