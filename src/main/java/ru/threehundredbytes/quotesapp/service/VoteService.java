package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.api.mapper.VoteMapper;
import ru.threehundredbytes.quotesapp.api.model.request.VoteRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.VoteResponseDTO;
import ru.threehundredbytes.quotesapp.exception.ConflictException;
import ru.threehundredbytes.quotesapp.exception.NotFoundException;
import ru.threehundredbytes.quotesapp.exception.UnprocessableEntityException;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.entity.Vote;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.UserRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.VoteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    public List<VoteResponseDTO> getAllVotes(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);

        return voteRepository.findAllByQuote(quote).stream()
                .map(VoteMapper::mapEntityToResponseDTO)
                .toList();
    }

    public VoteResponseDTO getVote(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Vote vote = voteRepository.findByQuoteAndUser(quote, user)
                .orElseGet(() -> createDefaultVote(quote, user));

        return VoteMapper.mapEntityToResponseDTO(vote);
    }

    public VoteResponseDTO createVote(Long quoteId, VoteRequestDTO requestDTO, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Vote vote = voteRepository.findByQuoteAndUser(quote, user)
                .orElseGet(() -> createDefaultVote(quote, user));

        long voteCountDelta = getVoteCountDelta(vote.getVoteState(), requestDTO.voteState());
        quote.setVoteCount(quote.getVoteCount() + voteCountDelta);
        quoteRepository.save(quote);

        vote.setVoteState(requestDTO.voteState());
        vote = voteRepository.save(vote);

        return VoteMapper.mapEntityToResponseDTO(vote);
    }

    public void deleteVote(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Vote vote = voteRepository.findByQuoteAndUser(quote, user).orElseThrow(NotFoundException::new);

        switch (vote.getVoteState()) {
            case UPVOTE -> quote.setVoteCount(quote.getVoteCount() - 1);
            case DOWNVOTE -> quote.setVoteCount(quote.getVoteCount() + 1);
        }

        quoteRepository.save(quote);
        voteRepository.delete(vote);
    }

    private Vote createDefaultVote(Quote quote, User user) {
        return Vote.builder()
                .quote(quote)
                .user(user)
                .build();
    }

    private long getVoteCountDelta(VoteState currentState, VoteState newState) {
        return switch (newState) {
            case UPVOTE -> switch (currentState) {
                case DOWNVOTE -> 2;
                case NOT_VOTED -> 1;
                case UPVOTE -> throw new ConflictException();
            };
            case DOWNVOTE -> switch (currentState) {
                case UPVOTE -> -2;
                case NOT_VOTED -> -1;
                case DOWNVOTE -> throw new ConflictException();
            };
            case NOT_VOTED -> throw new UnprocessableEntityException();
        };
    }
}
