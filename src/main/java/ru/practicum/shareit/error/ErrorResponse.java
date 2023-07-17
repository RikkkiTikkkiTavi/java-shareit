package ru.practicum.shareit.error;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private final String error;

    public String getError() {
        return error;
    }
}
