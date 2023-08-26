package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingValidateException;
import ru.practicum.shareit.booking.exception.UnsupportedStateException;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private BookingResponseDto responseDto;

    private BookingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        ItemDto itemDto = new ItemDto(1, "1", "1", true, 0);
        UserDto userDto = new UserDto(1, "1", "1");
        responseDto = new BookingResponseDto(1, 1, LocalDateTime.of(2010, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 1, 1, 10, 1), itemDto, userDto, Status.WAITING);
        requestDto = new BookingRequestDto(1, LocalDateTime.of(2010, 1, 1, 1, 1, 1),
                LocalDateTime.of(2030, 1, 1, 1, 10, 1));
    }

    @Test
    void addBooking() throws Exception {
        when(bookingService.addBooking(1, requestDto)).thenReturn(responseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", responseDto));
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(1, 1, true)).thenReturn(responseDto);

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(match("$", responseDto));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(1, 1)).thenReturn(responseDto);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(match("$", responseDto));
    }


    @Test
    void getBookingsByBooker() throws Exception {
        when(bookingService.getBookingsByBooker(1, "ALL", 0, 10)).thenReturn(List.of(responseDto));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", responseDto));
    }

    @Test
    void getBookingsByOwner() throws Exception {
        when(bookingService.getBookingsByOwner(1, "ALL", 0, 10)).thenReturn(List.of(responseDto));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(match("$[0]", responseDto));
    }

    @Test
    void throwBookingNotFoundExceptionGetStatus404() throws Exception {
        when(bookingService.approveBooking(1, 1, true)).thenThrow(BookingNotFoundException.class);

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    void throwBookingValidateExceptionGetStatus400() throws Exception {
        when(bookingService.approveBooking(1, 1, true)).thenThrow(BookingValidateException.class);

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void badPathGetStatus404() throws Exception {
        when(bookingService.approveBooking(1, 1, true)).thenReturn(responseDto);

        mvc.perform(patch("/book/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    void throwUnsupportedStateExceptionGetStatus400() throws Exception {
        when(bookingService.approveBooking(1, 1, true)).thenThrow(UnsupportedStateException.class);

        mvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest());
    }


    private static ResultMatcher match(String prefix, BookingResponseDto responseDto) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(responseDto.getId()),
                jsonPath(prefix + ".start").value(responseDto.getStart().toString()),
                jsonPath(prefix + ".end").value(responseDto.getEnd().toString()),
                jsonPath(prefix + ".status").value(responseDto.getStatus().toString()));
    }
}