package com.kameleoon.kameleoontrialtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kameleoon.kameleoontrialtask.dto.QuoteDTO;
import com.kameleoon.kameleoontrialtask.rest.QuoteRestController;
import com.kameleoon.kameleoontrialtask.service.QuoteService;
import com.kameleoon.kameleoontrialtask.service.UserService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema-test-start.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-test-end.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@NoArgsConstructor
public class QuoteRestControllerTest {
    private final static String EMPTY_LINE = "";
    private final static String PATH_TO_ALL_QUOTES_JSON = "src/test/resources/all-quotes.json";
    private final static String PATH_TO_ONE_QUOTE_JSON = "src/test/resources/one-quote.json";
    private final static String PATH_TO_TOP_10_QUOTES_JSON = "src/test/resources/top-10-quotes.json";
    private final static String PATH_TO_FLOP_10_QUOTES_JSON = "src/test/resources/flop-10-quotes.json";
    private final static String PATH_TO_LAST_QUOTE_JSON = "src/test/resources/last-quote.json";
    private final static String PATH_TO_DELETE_QUOTE_RESPONSE_JSON = "src/test/resources/delete-quote-response.json";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private QuoteRestController quoteRestController;
    @Autowired
    private QuoteService quoteService;
    @MockBean
    private UserService userService;

    @Test
    public void getAllQuotesShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_ALL_QUOTES_JSON)));

        this.mockMvc
            .perform(get("/api/quotes/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getQuoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_ONE_QUOTE_JSON)));

        this.mockMvc
            .perform(get("/api/quotes/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getRandomQuoteShouldReturnStatus200AndOneQuoteFromAll() throws Exception {
        final QuoteDTO randomQuote = quoteRestController.getRandomQuote();
        final List<QuoteDTO> allQuotes = quoteRestController.getAllQuotes();

        this.mockMvc
            .perform(get("/api/quotes/random"))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertTrue(allQuotes.contains(randomQuote));
    }

    @Test
    public void getTop10QuotesShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_TOP_10_QUOTES_JSON)));

        this.mockMvc
            .perform(get("/api/quotes/top10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getFlop10QuotesShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_FLOP_10_QUOTES_JSON)));

        this.mockMvc
            .perform(get("/api/quotes/flop10"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void getLastQuoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_LAST_QUOTE_JSON)));

        this.mockMvc
            .perform(get("/api/quotes/last"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void addNewQuoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");

        final QuoteDTO expectedQuote = QuoteDTO.builder()
            .id(0)
            .quote("test-quote")
            .creator("admin")
            .dateOfCreation(LocalDateTime.now())
            .votes(0)
            .build();

        final QuoteDTO actualQuote = quoteRestController.addNewQuote(expectedQuote);

        final ObjectMapper mapperForBody = new ObjectMapper().registerModule(new JavaTimeModule());
        final String json = mapperForBody.writeValueAsString(expectedQuote);

        this.mockMvc
            .perform(post("/api/quotes/add-quote").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertEquals(actualQuote, expectedQuote);
    }

    @Test
    public void modifyQuoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");

        final QuoteDTO expectedQuote = QuoteDTO.builder()
            .id(0)
            .quote("test-quote")
            .creator("admin")
            .dateOfCreation(LocalDateTime.now())
            .votes(0)
            .build();

        final QuoteDTO actualQuote = quoteRestController.addNewQuote(expectedQuote);

        final ObjectMapper mapperForBody = new ObjectMapper().registerModule(new JavaTimeModule());
        final String json = mapperForBody.writeValueAsString(expectedQuote);

        this.mockMvc
            .perform(post("/api/quotes/modify-quote").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertEquals(actualQuote, expectedQuote);
    }

    @Test
    public void deleteQuoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");

        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_DELETE_QUOTE_RESPONSE_JSON)));

        this.mockMvc
            .perform(delete("/api/quotes/delete/{quoteId}", 5))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }

    @Test
    public void upvoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        final QuoteDTO expectedQuote = quoteService.findQuoteById(1);
        expectedQuote.setVotes(expectedQuote.getVotes() + 1);
        expectedQuote.setDateOfCreation(LocalDateTime.now());

        final QuoteDTO actualQuote = (QuoteDTO) quoteRestController.upvote(1);

        this.mockMvc
            .perform(get("/api/quotes/upvote/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertEquals(expectedQuote.getVotes(), actualQuote.getVotes());
    }

    @Test
    public void downvoteShouldReturnStatus200AndCorrectJSON() throws Exception {
        final QuoteDTO expectedQuote = quoteService.findQuoteById(1);
        expectedQuote.setVotes(expectedQuote.getVotes() - 1);
        expectedQuote.setDateOfCreation(LocalDateTime.now());

        final QuoteDTO actualQuote = (QuoteDTO) quoteRestController.downvote(1);

        this.mockMvc
            .perform(get("/api/quotes/downvote/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertEquals(expectedQuote.getVotes(), actualQuote.getVotes());
    }
}
