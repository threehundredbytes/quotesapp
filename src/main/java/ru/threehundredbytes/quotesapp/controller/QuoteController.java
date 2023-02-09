package ru.threehundredbytes.quotesapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.service.QuoteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    // Pagination and sorting to get top/flop 10
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @GetMapping("/random")
    @ResponseStatus(HttpStatus.OK)
    public Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Quote createQuote(@RequestBody Quote quote) {
        return quoteService.createQuote(quote);
    }

    @PutMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.OK)
    public Quote updateQuoteById(@PathVariable Long quoteId, @RequestBody Quote quote) {
        return quoteService.updateQuote(quoteId, quote);
    }

    @DeleteMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuoteById(@PathVariable Long quoteId) {
        quoteService.deleteQuote(quoteId);
    }
}
