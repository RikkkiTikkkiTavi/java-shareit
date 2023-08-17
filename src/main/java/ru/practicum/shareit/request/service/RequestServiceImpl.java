package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.validator.RequestValidator;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    RequestRepository requestRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    @Transactional
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) {

        RequestValidator.checkDescription(itemRequestDto);
        itemRequestDto.setRequestor(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto)));
    }

    public List<ItemRequestDto> getOwnerRequests(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        List<ItemRequest> requests = requestRepository.findByRequestor_Id(userId);
        return requests.stream().map((req) ->
        {
            ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(req);
            requestDto.setItems(ItemMapper.toItemsDto(itemRepository.findByItemRequest_Id(req.getId())));
            return requestDto;
        }).collect(Collectors.toList());
    }

    public List<ItemRequestDto> getNotOwnerRequests(long userId, int from, int size) {
        RequestValidator.checkFromAndSize(from, size);
        PageRequest request = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> page = requestRepository.findByRequestor_IdNot(userId, request);
        return page.stream().map((req) ->
        {
            ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(req);
            requestDto.setItems(ItemMapper.toItemsDto(itemRepository.findByItemRequest_Id(req.getId())));
            return requestDto;
        }).collect(Collectors.toList());
    }
    
    public ItemRequestDto findById(long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        List<ItemDto> items = ItemMapper.toItemsDto(itemRepository.findByItemRequest_Id(requestId));
        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new ItemNotFoundException("Такого запроса не существует")));
        requestDto.setItems(items);
        return requestDto;
    }
}
