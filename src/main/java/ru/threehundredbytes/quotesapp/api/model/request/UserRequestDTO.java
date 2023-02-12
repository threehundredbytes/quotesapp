package ru.threehundredbytes.quotesapp.api.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank
        @Size(min = 3, max = 32)
        String username,

        @Email
        @NotBlank
        @Size(min = 3, max = 32)
        String email,

        @NotBlank
        @Size(min = 8, max = 128)
        String password
) {
}
