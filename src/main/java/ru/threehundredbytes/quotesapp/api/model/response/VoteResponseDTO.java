package ru.threehundredbytes.quotesapp.api.model.response;

import lombok.Builder;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;

@Builder
public record VoteResponseDTO(
        Long userId,
        String username,
        VoteState voteState
) {
}
