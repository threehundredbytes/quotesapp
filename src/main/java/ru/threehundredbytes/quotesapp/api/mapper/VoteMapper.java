package ru.threehundredbytes.quotesapp.api.mapper;

import ru.threehundredbytes.quotesapp.api.model.response.VoteResponseDTO;
import ru.threehundredbytes.quotesapp.persistence.entity.Vote;

public class VoteMapper {
    private VoteMapper() {
    }

    public static VoteResponseDTO mapEntityToResponseDTO(Vote vote) {
        return VoteResponseDTO.builder()
                .userId(vote.getUser().getId())
                .username(vote.getUser().getUsername())
                .voteState(vote.getVoteState())
                .build();
    }
}
