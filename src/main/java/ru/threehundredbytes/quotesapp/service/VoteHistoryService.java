package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.api.mapper.VoteHistoryMapper;
import ru.threehundredbytes.quotesapp.api.model.response.VoteHistoryResponseDTO;
import ru.threehundredbytes.quotesapp.exception.NotFoundException;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteHistory;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.VoteHistoryRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteHistoryService {
    private final VoteHistoryRepository voteHistoryRepository;
    private final QuoteRepository quoteRepository;

    public List<VoteHistoryResponseDTO> getAllByQuoteId(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);

        return voteHistoryRepository.findAllByQuote(quote).stream()
                .map(VoteHistoryMapper::mapEntityToResponseDto)
                .toList();
    }

    @Scheduled(cron = "@midnight")
    private void saveVoteHistory() {
        List<VoteHistory> voteHistories = quoteRepository.findAll().stream()
                .map(quote -> VoteHistory.builder()
                        .date(LocalDate.now())
                        .quote(quote)
                        .voteCount(quote.getVoteCount())
                        .build())
                .toList();

        voteHistoryRepository.saveAll(voteHistories);
    }
}
