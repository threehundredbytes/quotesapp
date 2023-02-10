package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.response.VoteResponseDTO;
import ru.threehundredbytes.quotesapp.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes/{quoteId}/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VoteResponseDTO> getAllVotes(@PathVariable Long quoteId) {
        return voteService.getAllVotes(quoteId);
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponseDTO getVote(@PathVariable Long quoteId, @PathVariable Long userId) {
        return voteService.getVote(quoteId, userId);
    }

    @PostMapping("/up")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponseDTO upVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        return voteService.upVote(quoteId, userId);
    }

    @PostMapping("/down")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponseDTO downVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        return voteService.downVote(quoteId, userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        voteService.deleteVote(quoteId, userId);
    }
}
