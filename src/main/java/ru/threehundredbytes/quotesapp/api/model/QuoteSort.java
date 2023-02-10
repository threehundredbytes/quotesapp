package ru.threehundredbytes.quotesapp.api.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum QuoteSort {
    VOTE_ASC(Sort.by(Sort.Direction.ASC, "voteCounter")),
    VOTE_DESC(Sort.by(Sort.Direction.DESC, "voteCounter"));

    private final Sort sort;
}
