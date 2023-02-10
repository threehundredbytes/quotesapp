package ru.threehundredbytes.quotesapp.test.integration;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.threehundredbytes.quotesapp.util.ResourceUtils.getResourceFileAsString;

@SpringBootTest
@Sql(value = { "/sql/quote/insertion.sql", "/sql/user/insertion.sql", "/sql/vote/insertion.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = { "/sql/vote/deletion.sql", "/sql/user/deletion.sql", "/sql/quote/deletion.sql" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VoteIntegrationTest extends BaseIntegrationTest {
    @Autowired
    QuoteRepository quoteRepository;

    @DisplayName("getVoteState() tests")
    @Nested
    class GetVoteStateTest {
        @Test
        @SneakyThrows
        void getVoteState_quoteIsNotVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/not_voted.json");

            mockMvc.perform(get("/api/v1/quotes/5/votes?userId=6"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVoteState_quoteIsDownVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/downvote.json");

            mockMvc.perform(get("/api/v1/quotes/4/votes?userId=6"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVoteState_quoteIsUpVote_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/upvote.json");

            mockMvc.perform(get("/api/v1/quotes/1/votes?userId=6"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getVoteState_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(get("/api/v1/quotes/0/votes?userId=6"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void getVoteState_userDoesNotExists_isNotFound() {
            mockMvc.perform(get("/api/v1/quotes/1/votes?userId=0"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("upVote() tests")
    @Nested
    class UpVoteTest {
        @Test
        @SneakyThrows
        void upVote_quoteIsNotVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/upvote.json");

            long quoteId = 5L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes/up?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(1);
        }

        @Test
        @SneakyThrows
        void upVote_quoteIsDownVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/upvote.json");

            long quoteId = 4L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes/up?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(1);
        }

        @Test
        @SneakyThrows
        void upVote_quoteIsUpVoted_isConflict() {
            mockMvc.perform(post("/api/v1/quotes/1/votes/up?userId=6"))
                    .andExpect(status().isConflict());
        }

        @Test
        @SneakyThrows
        void upVote_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(post("/api/v1/quotes/0/votes/up?userId=6"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void upVote_userDoesNotExists_isNotFound() {
            mockMvc.perform(post("/api/v1/quotes/5/votes/up?userId=0"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("downVote() tests")
    @Nested
    class DownVoteTest {
        @Test
        @SneakyThrows
        void downVote_quoteIsNotVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/downvote.json");

            long quoteId = 5L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes/down?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(-1);
        }

        @Test
        @SneakyThrows
        void downVote_quoteIsUpVoted_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/vote/downvote.json");

            long quoteId = 1L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes/down?userId=%d", quoteId, userId);

            mockMvc.perform(post(requestUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(-1);
        }

        @Test
        @SneakyThrows
        void downVote_quoteIsDownVoted_isConflict() {
            mockMvc.perform(post("/api/v1/quotes/4/votes/down?userId=6"))
                    .andExpect(status().isConflict());
        }

        @Test
        @SneakyThrows
        void downVote_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(post("/api/v1/quotes/0/votes/down?userId=6"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void downVote_userDoesNotExists_isNotFound() {
            mockMvc.perform(post("/api/v1/quotes/5/votes/down?userId=0"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("deleteVote() tests")
    @Nested
    class DeleteVoteTest {
        @Test
        @SneakyThrows
        void deleteVote_quoteIsUpVoted_isNoContent() {
            long quoteId = 1L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(delete(requestUrl))
                    .andExpect(status().isNoContent());

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(0);
        }

        @Test
        @SneakyThrows
        void deleteVote_quoteIsDownVoted_isNoContent() {
            long quoteId = 4L;
            long userId = 6L;

            String requestUrl = String.format("/api/v1/quotes/%d/votes?userId=%d", quoteId, userId);

            mockMvc.perform(delete(requestUrl))
                    .andExpect(status().isNoContent());

            Quote quote = quoteRepository.findById(quoteId).orElseThrow();

            Assertions.assertThat(quote.getVoteCounter()).isEqualTo(0);
        }


        @Test
        @SneakyThrows
        void deleteVote_quoteIsNotVoted_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/5/votes?userId=6"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void deleteVote_quoteDoesNotExists_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/0/votes?userId=6"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @SneakyThrows
        void deleteVote_userDoesNotExists_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/1/votes?userId=0"))
                    .andExpect(status().isNotFound());
        }
    }
}
