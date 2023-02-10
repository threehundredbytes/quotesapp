package ru.threehundredbytes.quotesapp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.entity.VoteHistory;
import ru.threehundredbytes.quotesapp.persistence.entity.id.VoteHistoryId;

import java.util.List;

public interface VoteHistoryRepository extends JpaRepository<VoteHistory, VoteHistoryId> {
    List<VoteHistory> findAllByQuote(Quote quote);
}
