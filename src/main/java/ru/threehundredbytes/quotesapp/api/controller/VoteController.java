package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.VoteDTO;
import ru.threehundredbytes.quotesapp.service.VoteService;

@RestController
@RequestMapping("/api/v1/quotes/{quoteId}/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VoteDTO getVoteState(@PathVariable Long quoteId, @RequestParam Long userId) {
        return new VoteDTO(voteService.getVoteState(quoteId, userId));
    }

    @PostMapping("/up")
    @ResponseStatus(HttpStatus.OK)
    public VoteDTO upVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        return new VoteDTO(voteService.upVote(quoteId, userId));
    }

    @PostMapping("/down")
    @ResponseStatus(HttpStatus.OK)
    public VoteDTO downVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        return new VoteDTO(voteService.downVote(quoteId, userId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        voteService.deleteVote(quoteId, userId);
    }
}
