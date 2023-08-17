package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    RequestService requestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemRequestDto itemRequest) {
        return requestService.addRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getOwnerRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getNoOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return requestService.getNotOwnerRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable("requestId") long requestId) {
        return requestService.findById(userId, requestId);
    }
}
