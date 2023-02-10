package ru.threehundredbytes.quotesapp.api.model.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VoteHistoryResponseDTO(LocalDate date, Long voteCount) {

}
