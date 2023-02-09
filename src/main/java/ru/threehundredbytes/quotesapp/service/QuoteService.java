package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.exception.NotFoundException;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Quote getRandomQuote() {
        long quotesCount = quoteRepository.count();

        if (quotesCount == 0) {
            throw new NotFoundException();
        }

        int randomPage = (int) ThreadLocalRandom.current().nextLong(quotesCount);
        Page<Quote> page = quoteRepository.findAll(PageRequest.of(randomPage, 1));

        Quote randomQuote = null;

        if (page.hasContent()) {
            randomQuote = page.getContent().get(0);
        }

        return randomQuote;
    }

    public Quote createQuote(Quote quote) {
        return quoteRepository.save(quote);
    }

    public Quote updateQuote(Long quoteId, Quote quote) {
        if (!quoteRepository.existsById(quoteId)) {
            throw new NotFoundException();
        }

        quote.setId(quoteId);

        return quoteRepository.save(quote);
    }

    public void deleteQuote(Long quoteId) {
        if (!quoteRepository.existsById(quoteId)) {
            throw new NotFoundException();
        }

        quoteRepository.deleteById(quoteId);
    }
}
