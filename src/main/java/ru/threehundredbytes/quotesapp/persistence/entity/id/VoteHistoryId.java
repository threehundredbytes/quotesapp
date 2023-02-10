package ru.threehundredbytes.quotesapp.persistence.entity.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteHistoryId implements Serializable {
    private LocalDate date;
    private Long quote;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteHistoryId that)) return false;
        return date.equals(that.date) && quote.equals(that.quote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, quote);
    }
}