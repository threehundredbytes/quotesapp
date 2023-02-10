package ru.threehundredbytes.quotesapp.api.mapper;

import ru.threehundredbytes.quotesapp.api.model.response.VoteHistoryResponseDTO;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteHistory;

public class VoteHistoryMapper {
    private VoteHistoryMapper() {
    }

    public static VoteHistoryResponseDTO mapEntityToResponseDto(VoteHistory voteHistory) {
        return VoteHistoryResponseDTO.builder()
                .date(voteHistory.getDate())
                .voteCount(voteHistory.getVoteCount())
                .build();
    }
}
