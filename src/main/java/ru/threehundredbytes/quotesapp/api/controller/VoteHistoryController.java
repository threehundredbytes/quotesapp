package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.threehundredbytes.quotesapp.api.model.response.VoteHistoryResponseDTO;
import ru.threehundredbytes.quotesapp.service.VoteHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoteHistoryController {
    private final VoteHistoryService voteHistoryService;

    @GetMapping("/api/v1/quotes/{quoteId}/votes/history")
    public List<VoteHistoryResponseDTO> getAllByQuoteId(@PathVariable Long quoteId) {
        return voteHistoryService.getAllByQuoteId(quoteId);
    }
}
