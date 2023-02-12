package ru.threehundredbytes.quotesapp.api.model.request;

import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;

import javax.validation.constraints.NotNull;

public record VoteRequestDTO(
        @NotNull
        VoteState voteState
) {
}
