package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestServiceImplTest {

    @Mock
    RequestRepository requestRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private RequestServiceImpl requestService;
    private User user;
    private ItemRequestDto itemRequestDto;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = new User(1, "name", "email");
        UserDto userDto = new UserDto(1, "name", "email");
        Item item = new Item(1, user, "name", "desc", true, itemRequest);
        itemRequest = new ItemRequest(1, "desc", user, LocalDateTime.now().withNano(0), List.of(item));
        ItemDto itemDto = new ItemDto(1, "name", "desc", true, 0);
        itemRequestDto = new ItemRequestDto(1, "desc", userDto, LocalDateTime.now().withNano(0), List.of(itemDto));
    }

    @Test
    void addNewRequestThrowExceptionIfUserNotExist() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь не найден"));
        assertThrows(UserNotFoundException.class, () -> requestService.addRequest(1, itemRequestDto));
    }

    @Test
    void addRequestReturnItemRequestDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        assertEquals(itemRequestDto, requestService.addRequest(1, itemRequestDto));
    }

    @Test
    void getOwnerRequestsThrowExceptionIfUserNotExist() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь не найден"));
        assertThrows(UserNotFoundException.class, () -> requestService.getOwnerRequests(1));
    }

    @Test
    void getOwnerRequestsReturnListOfItemRequestDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findByRequestor_Id(anyLong())).thenReturn(List.of(itemRequest));
        assertEquals(List.of(itemRequestDto), requestService.getOwnerRequests(1));
    }

    @Test
    void getNotOwnerRequestsReturnListOfItemRequestDto() {
        Page<ItemRequest> itemRequests = new PageImpl<>(List.of(itemRequest));
        when(requestRepository.findByRequestor_IdNot(anyLong(), any(PageRequest.class))).thenReturn(itemRequests);
        assertEquals(List.of(itemRequestDto), requestService.getNotOwnerRequests(1, 0, 10));
    }

    @Test
    void findByIdThrowExceptionIfUserNotExist() {
        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException("Пользователь не найден"));
        assertThrows(UserNotFoundException.class, () -> requestService.findById(1, 1));
    }

    @Test
    void findByIdThrowExceptionIfRequestNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(anyLong())).thenThrow(new ItemNotFoundException(("Запрос не найден")));
        assertThrows(ItemNotFoundException.class, () -> requestService.findById(1, 1));
    }

    @Test
    void findByIdReturnItemRequestDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        assertEquals(itemRequestDto, requestService.findById(1, 1));
    }
}