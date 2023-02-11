package ru.threehundredbytes.quotesapp.api.mapper;

import ru.threehundredbytes.quotesapp.api.model.response.QuoteResponseDTO;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;

public class QuoteMapper {
    private QuoteMapper() {
    }

    public static QuoteResponseDTO mapEntityToResponseDTO(Quote quote) {
        return QuoteResponseDTO.builder()
                .id(quote.getId())
                .text(quote.getText())
                .voteCount(quote.getVoteCount())
                .postedByUserId(quote.getPostedBy().getId())
                .postedByUsername(quote.getPostedBy().getUsername())
                .createdAt(quote.getCreatedAt())
                .modifiedAt(quote.getModifiedAt())
                .build();
    }
}
