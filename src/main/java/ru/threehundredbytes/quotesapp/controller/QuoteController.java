package ru.threehundredbytes.quotesapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.service.QuoteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    // Pagination and sorting to get top/flop 10
    @GetMapping("/quotes")
    @ResponseStatus(HttpStatus.OK)
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @GetMapping("/quotes/random")
    @ResponseStatus(HttpStatus.OK)
    public Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    @PostMapping("/quotes")
    @ResponseStatus(HttpStatus.CREATED)
    public Quote createQuote(@RequestBody Quote quote) {
        return quoteService.createQuote(quote);
    }

    @PutMapping("/quotes/{quoteId}")
    @ResponseStatus(HttpStatus.OK)
    public Quote updateQuoteById(@PathVariable Long quoteId, @RequestBody Quote quote) {
        return quoteService.updateQuote(quoteId, quote);
    }

    @DeleteMapping("/quotes/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuoteById(@PathVariable Long quoteId) {
        quoteService.deleteQuote(quoteId);
    }
}
