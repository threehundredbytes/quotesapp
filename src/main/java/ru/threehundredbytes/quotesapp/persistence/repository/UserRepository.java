package ru.threehundredbytes.quotesapp.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.threehundredbytes.quotesapp.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
