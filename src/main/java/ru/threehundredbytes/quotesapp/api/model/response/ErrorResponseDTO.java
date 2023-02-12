package ru.threehundredbytes.quotesapp.api.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String error;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private Map<String, String> errors = new HashMap<>();

    private String message;
    private String path;
}
