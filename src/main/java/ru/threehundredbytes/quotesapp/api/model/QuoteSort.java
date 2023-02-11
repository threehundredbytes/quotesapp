package ru.threehundredbytes.quotesapp.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum QuoteSort {
    VOTE_ASC(Sort.by(Sort.Direction.ASC, "voteCount")),
    VOTE_DESC(Sort.by(Sort.Direction.DESC, "voteCount"));

    private final Sort sort;
}
