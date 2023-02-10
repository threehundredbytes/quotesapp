package ru.threehundredbytes.quotesapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.threehundredbytes.quotesapp.api.mapper.UserMapper;
import ru.threehundredbytes.quotesapp.api.model.request.UserRequestDTO;
import ru.threehundredbytes.quotesapp.api.model.response.UserResponseDTO;
import ru.threehundredbytes.quotesapp.exception.ConflictException;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.username())) {
            throw new ConflictException();
        }

        if (userRepository.existsByEmail(requestDTO.username())) {
            throw new ConflictException();
        }

        User user = User.builder()
                .username(requestDTO.username())
                .email(requestDTO.email())
                .password(requestDTO.password())
                .build();

        return UserMapper.mapEntityToResponseDTO(userRepository.save(user));
    }
}
