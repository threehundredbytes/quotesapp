package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.api.mapper.QuoteMapper;
import ru.threehundredbytes.quotesapp.api.model.request.QuoteRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.QuoteResponseDTO;
import ru.threehundredbytes.quotesapp.exception.NotFoundException;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    public List<QuoteResponseDTO> getAllQuotes(PageRequest pageRequest) {
        return quoteRepository.findAll(pageRequest)
                .map(QuoteMapper::mapEntityToResponseDTO)
                .toList();
    }

    public QuoteResponseDTO getRandomQuote() {
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

        return QuoteMapper.mapEntityToResponseDTO(randomQuote);
    }

    public QuoteResponseDTO createQuote(QuoteRequestDTO requestDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Quote quote = Quote.builder()
                .text(requestDTO.text())
                .postedBy(user)
                .build();

        return QuoteMapper.mapEntityToResponseDTO(quoteRepository.save(quote));
    }

    public QuoteResponseDTO updateQuote(Long quoteId, QuoteRequestDTO requestDTO) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);

        quote.setText(requestDTO.text());

        return QuoteMapper.mapEntityToResponseDTO(quoteRepository.save(quote));
    }

    public void deleteQuote(Long quoteId) {
        if (!quoteRepository.existsById(quoteId)) {
            throw new NotFoundException();
        }

        quoteRepository.deleteById(quoteId);
    }
}
