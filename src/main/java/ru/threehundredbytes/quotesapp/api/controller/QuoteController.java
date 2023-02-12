package ru.threehundredbytes.quotesapp.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.threehundredbytes.quotesapp.api.model.QuoteSort;
import ru.threehundredbytes.quotesapp.api.model.request.QuoteRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.QuoteResponseDTO;
import ru.threehundredbytes.quotesapp.service.QuoteService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuoteResponseDTO> getAllQuotes(
            @RequestParam(name = "offset",required = false, defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit,
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
    public QuoteResponseDTO createQuote(@Valid @RequestBody QuoteRequestDTO requestDTO, @RequestParam Long userId) {
        return quoteService.createQuote(requestDTO, userId);
    }

    @PutMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.OK)
    public QuoteResponseDTO updateQuoteById(@PathVariable Long quoteId, @Valid @RequestBody QuoteRequestDTO requestDTO) {
        return quoteService.updateQuote(quoteId, requestDTO);
    }

    @DeleteMapping("/{quoteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuoteById(@PathVariable Long quoteId) {
        quoteService.deleteQuote(quoteId);
    }
}
