package ru.threehundredbytes.quotesapp.test.integration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.threehundredbytes.quotesapp.persistence.entity.User;
import ru.threehundredbytes.quotesapp.persistence.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.threehundredbytes.quotesapp.util.ResourceUtils.getResourceFileAsString;

@SpringBootTest
@Sql(value = "/sql/user/insertion.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/user/deletion.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIntegrationTest extends BaseIntegrationTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @SneakyThrows
    void createUser_isCreated() {
        String requestContent = getResourceFileAsString("json/request/user/create.json");
        String expectedResponse = getResourceFileAsString("json/response/user/created.json");

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @SneakyThrows
    void createUser_userWithSameUsernameExists_isConflict() {
        userRepository.save(User.builder()
                .username("user")
                .build());

        String requestContent = getResourceFileAsString("json/request/user/create.json");

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void createUser_userWithSameEmailExists_isConflict() {
        userRepository.save(User.builder()
                .username("user")
                .email("user@mail.com")
                .build());

        String requestContent = getResourceFileAsString("json/request/user/create.json");

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andExpect(status().isConflict());
    }
}
