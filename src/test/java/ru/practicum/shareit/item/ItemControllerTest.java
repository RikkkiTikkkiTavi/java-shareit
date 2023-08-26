package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;

    private CommentDto commentDto;

    private ItemBookingDto itemBookingDto;

    @BeforeEach
    void setUp() {
        User user = new User(1, "1", "email");
        itemDto = new ItemDto(1, "name", "desc", true, 1);
        commentDto = new CommentDto(1, "author", "text", LocalDateTime.now().withNano(0));
        itemBookingDto = new ItemBookingDto(1, "name", "description", true, null, null, null);
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addNewItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", itemDto));
    }

    @Test
    void editItem() throws Exception {
        when(itemService.editItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", itemDto));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemBookingDto);
        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", itemBookingDto));

    }

    @Test
    void getUserItems() throws Exception {
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(itemBookingDto));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", itemBookingDto));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems(anyString())).thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "text")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", itemDto));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated().toString()));
    }

    @Test
    void throwItemNotFoundExceptionGetStatus404() throws Exception {
        when(itemService.searchItems(anyString())).thenThrow(ItemNotFoundException.class);
        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "text")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void throwItemValidateExceptionGetStatus400() throws Exception {
        when(itemService.searchItems(anyString())).thenThrow(ItemValidationException.class);
        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("text", "text")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    private static ResultMatcher match(String prefix, ItemDto itemDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(itemDto.getId()),
                jsonPath(prefix + ".name").value(itemDto.getName()),
                jsonPath(prefix + ".description").value(itemDto.getDescription()),
                jsonPath(prefix + ".available").value(itemDto.getAvailable()));
    }

    private static ResultMatcher match(String prefix, ItemBookingDto itemBookingDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(itemBookingDto.getId()),
                jsonPath(prefix + ".name").value(itemBookingDto.getName()),
                jsonPath(prefix + ".description").value(itemBookingDto.getDescription()),
                jsonPath(prefix + ".available").value(itemBookingDto.getAvailable()));
    }

}