package ru.threehundredbytes.quotesapp.test.integration;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.threehundredbytes.quotesapp.util.ResourceUtils.getResourceFileAsString;

@SpringBootTest
@Sql(value = { "/sql/user/insertion.sql", "/sql/quote/insertion.sql", "/sql/vote/insertion.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/sql/vote/deletion.sql", "/sql/quote/deletion.sql", "/sql/user/deletion.sql" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VoteIntegrationTest extends BaseIntegrationTest {
    @Autowired
    QuoteRepository quoteRepository;

    @DisplayName("getAllVotes() tests")
    @Nested
    class GetAllVotesTest {
        @Test
        @SneakyThrows
        void getAllVotes_quoteWithId2_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/all.json");

            mockMvc.perform(get("/api/v1/quotes/2/votes"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getAllVotes_quoteWithoutVotes_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/empty.json");

            mockMvc.perform(get("/api/v1/quotes/6/votes"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getAllVotes_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(get("/api/v1/quotes/0/votes"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("getVote() tests")
    @Nested
    class GetVoteStateTest {
        @Test
        @SneakyThrows
        void getVote_quoteIsNotVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/notVoted.json");

            mockMvc.perform(get("/api/v1/quotes/6/votes/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVote_quoteIsDownVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/downVote.json");

            mockMvc.perform(get("/api/v1/quotes/5/votes/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVote_quoteIsUpVote_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/upVote.json");

            mockMvc.perform(get("/api/v1/quotes/2/votes/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVote_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(get("/api/v1/quotes/0/votes/users/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void getVote_userDoesNotExists_isNotFound() {
            mockMvc.perform(get("/api/v1/quotes/2/votes/users/0"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("createVote() - upVote tests")
    @Nested
    class CreateVoteUpVoteTest {
        @Test
        @SneakyThrows
        void createVote_upVote_quoteIsNotVoted_isOk() {
            String requestContent = getResourceFileAsString("json/request/vote/upVote.json");
            String expectedResponse = getResourceFileAsString("json/response/vote/upVote.json");

            long quoteId = 6L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(1);
        }

        @Test
        @SneakyThrows
        void createVote_upVote_quoteIsDownVoted_isOk() {
            String requestContent = getResourceFileAsString("json/request/vote/upVote.json");
            String expectedResponse = getResourceFileAsString("json/response/vote/upVote.json");

            long quoteId = 5L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(1);
        }

        @Test
        @SneakyThrows
        void createVote_upVote_quoteIsUpVoted_isConflict() {
            String requestContent = getResourceFileAsString("json/request/vote/upVote.json");

            mockMvc.perform(post("/api/v1/quotes/2/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isConflict());
        }

        @Test
        @SneakyThrows
        void createVote_upVote_quoteDoesNotExists_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/upVote.json");

            mockMvc.perform(post("/api/v1/quotes/0/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void createVote_upVote_userDoesNotExists_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/upVote.json");

            mockMvc.perform(post("/api/v1/quotes/2/votes?userId=0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("createVote() - downVote tests")
    @Nested
    class CreateVoteDownVoteTest {
        @Test
        @SneakyThrows
        void createVote_downVote_quoteIsNotVoted_isOk() {
            String requestContent = getResourceFileAsString("json/request/vote/downVote.json");
            String expectedResponse = getResourceFileAsString("json/response/vote/downVote.json");

            long quoteId = 6L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(-1);
        }

        @Test
        @SneakyThrows
        void createVote_downVote_quoteIsUpVoted_isOk() {
            String requestContent = getResourceFileAsString("json/request/vote/downVote.json");
            String expectedResponse = getResourceFileAsString("json/response/vote/downVote.json");

            long quoteId = 2L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(-1);
        }

        @Test
        @SneakyThrows
        void createVote_downVote_quoteIsDownVoted_isConflict() {
            String requestContent = getResourceFileAsString("json/request/vote/downVote.json");

            mockMvc.perform(post("/api/v1/quotes/5/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isConflict());
        }

        @Test
        @SneakyThrows
        void createVote_downVote_quoteDoesNotExists_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/downVote.json");

            mockMvc.perform(post("/api/v1/quotes/0/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void createVote_downVote_userDoesNotExists_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/downVote.json");

            mockMvc.perform(post("/api/v1/quotes/5/votes?userId=0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("createVote() - notVoted tests")
    @Nested
    class CreateVoteNotVotedTest {
        @Test
        @SneakyThrows
        void createVote_upVoted_requestWithNotVoted_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/notVoted.json");

            mockMvc.perform(post("/api/v1/quotes/2/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @SneakyThrows
        void createVote_notVoted_requestWithNotVoted_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/notVoted.json");

            mockMvc.perform(post("/api/v1/quotes/6/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @SneakyThrows
        void createVote_downVoted_requestWithNotVoted_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/vote/notVoted.json");

            mockMvc.perform(post("/api/v1/quotes/5/votes?userId=1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @DisplayName("deleteVote() tests")
    @Nested
    class DeleteVoteTest {
        @Test
        @SneakyThrows
        void deleteVote_quoteIsUpVoted_isNoContent() {
            long quoteId = 2L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(delete(requestUrl))
                    .andExpect(status().isNoContent());

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(0);
        }

        @Test
        @SneakyThrows
        void deleteVote_quoteIsDownVoted_isNoContent() {
            long quoteId = 5L;
            long userId = 1L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(delete(requestUrl))
                    .andExpect(status().isNoContent());

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCount()).isEqualTo(0);
        }


        @Test
        @SneakyThrows
        void deleteVote_quoteIsNotVoted_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/6/votes?userId=1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void deleteVote_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/0/votes?userId=1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void deleteVote_userDoesNotExists_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/2/votes?userId=0"))
                    .andExpect(status().isNotFound());
        }
    }
}
