package ru.threehundredbytes.quotesapp.api.mapper;

import ru.threehundredbytes.quotesapp.api.model.response.UserResponseDTO;
import ru.threehundredbytes.quotesapp.persistence.entity.User;

public class UserMapper {
    private UserMapper() {
    }

    public static UserResponseDTO mapEntityToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
