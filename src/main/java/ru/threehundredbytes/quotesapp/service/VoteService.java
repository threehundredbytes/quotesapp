package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.exception.ConflictException;
import ru.threehundredbytes.quotesapp.exception.NotFoundException;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.entity.Vote;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteState;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.UserRepository;
import ru.threehundredbytes.quotesapp.persistence.repository.VoteRepository;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    public VoteState getVoteState(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        return voteRepository.findByQuoteAndUser(quote, user)
                .map(Vote::getVoteState)
                .orElse(VoteState.NOT_VOTED);
    }

    public VoteState upVote(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Vote vote = voteRepository.findByQuoteAndUser(quote, user)
                .orElseGet(() -> createDefaultVote(quote, user));

        switch (vote.getVoteState()) {
            case DOWNVOTE -> quote.setVoteCounter(quote.getVoteCounter() + 2);
            case NOT_VOTED -> quote.setVoteCounter(quote.getVoteCounter() + 1);
            case UPVOTE -> throw new ConflictException();
        }

        vote.setVoteState(VoteState.UPVOTE);

        quoteRepository.save(quote);

        return voteRepository.save(vote).getVoteState();
    }

    public VoteState downVote(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Vote vote = voteRepository.findByQuoteAndUser(quote, user)
                .orElseGet(() -> createDefaultVote(quote, user));

        switch (vote.getVoteState()) {
            case UPVOTE -> quote.setVoteCounter(quote.getVoteCounter() - 2);
            case NOT_VOTED -> quote.setVoteCounter(quote.getVoteCounter() - 1);
            case DOWNVOTE -> throw new ConflictException();
        }

        vote.setVoteState(VoteState.DOWNVOTE);

        quoteRepository.save(quote);

        return voteRepository.save(vote).getVoteState();
    }

    public void deleteVote(Long quoteId, Long userId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(NotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        Vote vote = voteRepository.findByQuoteAndUser(quote, user).orElseThrow(NotFoundException::new);

        switch (vote.getVoteState()) {
            case UPVOTE -> quote.setVoteCounter(quote.getVoteCounter() - 1);
            case DOWNVOTE -> quote.setVoteCounter(quote.getVoteCounter() + 1);
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
}
