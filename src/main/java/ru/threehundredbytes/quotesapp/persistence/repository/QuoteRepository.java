package ru.threehundredbytes.quotesapp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
