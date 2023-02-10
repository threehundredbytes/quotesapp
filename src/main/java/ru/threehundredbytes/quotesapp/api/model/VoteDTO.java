package ru.threehundredbytes.quotesapp.api.model;

import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;

public record VoteDTO(VoteState voteState) {
}
