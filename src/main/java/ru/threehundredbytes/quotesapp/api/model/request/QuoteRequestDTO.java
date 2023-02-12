package ru.threehundredbytes.quotesapp.api.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record QuoteRequestDTO(
        @NotBlank
        @Size(min = 8, max = 512)
        String text
) {
}
