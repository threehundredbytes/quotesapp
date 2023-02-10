package ru.threehundredbytes.quotesapp.api.model.request;

import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;

public record VoteRequestDTO(
        VoteState voteState
) {
}
