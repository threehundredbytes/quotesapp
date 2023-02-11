package ru.threehundredbytes.quotesapp.api.model.request;

public record UserRequestDTO(
        String username,
        String email,
        String password
) {
}
