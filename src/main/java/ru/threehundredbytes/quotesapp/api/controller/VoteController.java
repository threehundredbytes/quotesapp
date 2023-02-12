package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.request.VoteRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.VoteResponseDTO;
import ru.threehundredbytes.quotesapp.service.VoteService;

import javax.validation.Valid;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public VoteResponseDTO createVote(
            @PathVariable Long quoteId,
            @Valid @RequestBody VoteRequestDTO requestDTO,
            @RequestParam Long userId) {
        return voteService.createVote(quoteId, requestDTO, userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@PathVariable Long quoteId, @RequestParam Long userId) {
        voteService.deleteVote(quoteId, userId);
    }
}
