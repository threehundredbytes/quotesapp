package ru.threehundredbytes.quotesapp.test.integration;

import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import ru.threehundredbytes.quotesapp.persistence.entity.Quote;
import ru.threehundredbytes.quotesapp.persistence.repository.QuoteRepository;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.threehundredbytes.quotesapp.util.ResourceUtils.getResourceFileAsString;

@SpringBootTest
@Sql(value = "/sql/quote/insertion.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/quote/deletion.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class QuoteIntegrationTest extends BaseIntegrationTest {
    @Autowired
    QuoteRepository quoteRepository;

    @DisplayName("getAllQuotes() tests")
    @Nested
    class GetAllQuotesTest {
        @Test
        @SneakyThrows
        void getAllQuotes_isOk() {
            String expectedResponse = getResourceFileAsString("json/response/quote/all.json");

            mockMvc.perform(get("/api/v1/quotes"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void getAllQuotes_anyQuoteDoesNotExists_isOk() {
            quoteRepository.deleteAll();

            String expectedResponse = getResourceFileAsString("json/response/quote/empty.json");

            mockMvc.perform(get("/api/v1/quotes"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }
    }

    @DisplayName("getRandomQuote() Tests")
    @Nested
    class GetRandomQuoteTest {
        @Test
        @SneakyThrows
        void getRandomQuote_anyQuoteExists_isOk() {
            List<Quote> quotes = quoteRepository.findAll();
            List<String> quoteTexts = quotes.stream()
                    .map(Quote::getText)
                    .toList();

            mockMvc.perform(get("/api/v1/quotes/random"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.text").value(Matchers.in(quoteTexts)));
        }

        @Test
        @SneakyThrows
        void getRandomQuote_anyQuoteDoesNotExists_isNotFound() {
            quoteRepository.deleteAll();

            mockMvc.perform(get("/api/v1/quotes/random"))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("createQuote() tests")
    @Nested
    class CreateQuoteTest {
        @Test
        @SneakyThrows
        void createQuote_isCreated() {
            String requestContent = getResourceFileAsString("json/request/quote/create.json");
            String expectedResponse = getResourceFileAsString("json/response/quote/created.json");

            mockMvc.perform(post("/api/v1/quotes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(expectedResponse));
        }
    }

    @DisplayName("updateQuoteById() tests")
    @Nested
    class UpdateQuoteByIdTest {
        @Test
        @SneakyThrows
        void updateQuote_quoteExist_isOk() {
            String requestContent = getResourceFileAsString("json/request/quote/update.json");
            String expectedResponse = getResourceFileAsString("json/response/quote/updated.json");

            mockMvc.perform(put("/api/v1/quotes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse));
        }

        @Test
        @SneakyThrows
        void updateQuote_quoteDoesNotExists_isNotFound() {
            String requestContent = getResourceFileAsString("json/request/quote/update.json");

            mockMvc.perform(put("/api/v1/quotes/6")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("deleteQuoteById() tests")
    @Nested
    class DeleteQuoteByIdTest {
        @Test
        @SneakyThrows
        void deleteQuoteById_quoteExists_isNoContent() {
            mockMvc.perform(delete("/api/v1/quotes/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void deleteQuoteById_quoteDoesNotExist_isNotFound() {
            mockMvc.perform(delete("/api/v1/quotes/6"))
                    .andExpect(status().isNotFound());
        }
    }
}
