package ru.threehundredbytes.quotesapp.persistence.entity;

import lombok.*;
import ru.threehundredbytes.quotesapp.persistence.entity.id.VoteHistoryId;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(VoteHistoryId.class)
public class VoteHistory {
    @Id
    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    private Quote quote;

    @Column(updatable = false)
    private Long voteCount;
}
