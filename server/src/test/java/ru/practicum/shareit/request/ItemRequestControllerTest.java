package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto(1, "1", "1");
        requestDto = new ItemRequestDto(1, "Desc", userDto, LocalDateTime.now().withNano(0), null);
    }

    @Test
    void addRequest() throws Exception {
        when(requestService.addRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", requestDto));
    }

    @Test
    void getNoOwnerRequests() throws Exception {
        when(requestService.getNotOwnerRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", requestDto));
    }

    @Test
    void getOwnerRequests() throws Exception {
        when(requestService.getOwnerRequests(anyLong())).thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", requestDto));
    }

    @Test
    void getRequest() throws Exception {
        when(requestService.findById(anyLong(), anyLong())).thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", requestDto));
    }

    private static ResultMatcher match(String prefix, ItemRequestDto requestDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(requestDto.getId()),
                jsonPath(prefix + ".description").value(requestDto.getDescription()),
                jsonPath(prefix + ".created").value(requestDto.getCreated().toString()));
    }
}