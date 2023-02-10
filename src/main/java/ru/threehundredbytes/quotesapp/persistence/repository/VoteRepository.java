package ru.threehundredbytes.quotesapp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.entity.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByQuoteAndUser(Quote quote, User user);
}
