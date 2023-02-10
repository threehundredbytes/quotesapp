package ru.threehundredbytes.quotesapp.api.model.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record QuoteResponseDTO(
        Long id,
        String text,
        Long voteCounter,
        Long postedByUserId,
        String postedByUsername,
        LocalDate createdAt,
        LocalDate modifiedAt
) {
}
