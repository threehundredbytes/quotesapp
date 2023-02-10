package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.QuoteSort;
import ru.threehundredbytes.quotesapp.api.model.request.QuoteRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.QuoteResponseDTO;
import ru.threehundredbytes.quotesapp.service.QuoteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuoteResponseDTO> getAllQuotes(
            @RequestParam(name = "offset",required = false, defaultValue = "0") Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sort", required = false, defaultValue = "VOTE_ASC") QuoteSort quoteSort
    ) {
        return quoteService.getAllQuotes(PageRequest.of(offset, limit, quoteSort.getSort()));
    }

    @GetMapping("/random")
    @ResponseStatus(HttpStatus.OK)
    public QuoteResponseDTO getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuoteResponseDTO createQuote(@RequestBody QuoteRequestDTO requestDTO, @RequestParam Long userId) {
        return quoteService.createQuote(requestDTO, userId);
    }

    @PutMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.OK)
    public QuoteResponseDTO updateQuoteById(@PathVariable Long quoteId, @RequestBody QuoteRequestDTO requestDTO) {
        return quoteService.updateQuote(quoteId, requestDTO);
    }

    @DeleteMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuoteById(@PathVariable Long quoteId) {
        quoteService.deleteQuote(quoteId);
    }
}
