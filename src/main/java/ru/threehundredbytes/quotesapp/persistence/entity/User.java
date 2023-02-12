package ru.threehundredbytes.quotesapp.persistence.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, length = 32)
    private String username;

    @Column(unique = true, length = 32)
    private String email;

    @Column(length = 128)
    private String password;

    @CreationTimestamp
    private LocalDate createdAt;
}