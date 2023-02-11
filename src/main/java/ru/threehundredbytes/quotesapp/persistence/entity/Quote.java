package ru.threehundredbytes.quotesapp.persistence.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;

    @Builder.Default
    private Long voteCount = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    private User postedBy;

    @CreationTimestamp
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;
}
