package com.kameleoon.kameleoontrialtask;

import com.kameleoon.kameleoontrialtask.dto.QuoteDTO;
import com.kameleoon.kameleoontrialtask.dto.UserDTO;
import com.kameleoon.kameleoontrialtask.service.QuoteService;
import com.kameleoon.kameleoontrialtask.service.UserService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema-test-start.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/schema-test-end.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@NoArgsConstructor
public class QuoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private QuoteService quoteService;
    @MockBean
    private UserService userService;

    @Test
    public void firstPageShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    public void getTop10QuotesShouldReturnStatus200AndExactView() throws Exception {
        final UserDTO userDTO = UserDTO.builder()
            .id(1)
            .username("test-admin")
            .password("test-admin")
            .email("test-admin@gmail.com")
            .dateOfCreation(LocalDate.now())
            .build();

        this.mockMvc
            .perform(get("/top_10_quotes").flashAttr("logged", userDTO))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("top-10-quotes"));
    }

    @Test
    public void getFlop10QuotesShouldReturnStatus200AndExactView() throws Exception {
        final UserDTO userDTO = UserDTO.builder()
            .id(1)
            .username("test-admin")
            .password("test-admin")
            .email("test-admin@gmail.com")
            .dateOfCreation(LocalDate.now())
            .build();

        this.mockMvc
            .perform(get("/flop_10_quotes").flashAttr("logged", userDTO))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("flop-10-quotes"));
    }

    @Test
    public void getLastQuoteShouldReturnStatus200AndExactView() throws Exception {
        final UserDTO userDTO = UserDTO.builder()
            .id(1)
            .username("test-admin")
            .password("test-admin")
            .email("test-admin@gmail.com")
            .dateOfCreation(LocalDate.now())
            .build();

        this.mockMvc
            .perform(get("/last_quote").flashAttr("logged", userDTO))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("last-quote"));
    }

    @Test
    public void upvoteShouldReturnStatus3xxAndRedirect() throws Exception {
        this.mockMvc
            .perform(get("/{from}/upvote/{quoteId}", "index", 1))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    public void downvoteShouldReturnStatus3xxAndRedirect() throws Exception {
        this.mockMvc
            .perform(get("/{from}/downvote/{quoteId}", "index", 1))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    public void modifyQuoteShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/modify/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("modify-quote"));
    }

    @Test
    public void updateQuoteShouldReturnStatus3xxAndRedirect() throws Exception {
        this.mockMvc
            .perform(post("/update-quote/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    public void addNewQuoteShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/add-quote"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("add-new-quote"));
    }

    @Test
    public void saveNewQuoteShouldReturnStatus3xxAndRedirect() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");

        final QuoteDTO quoteDTO = QuoteDTO.builder()
            .id(quoteService.getAllQuotes().size())
            .quote("test-quote")
            .creator("admin")
            .dateOfCreation(LocalDateTime.now())
            .votes(0)
            .build();

        this.mockMvc
            .perform(post("/save-quote").flashAttr("quoteDTO", quoteDTO))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    public void deleteQuoteShouldReturnStatus3xxAndRedirect() throws Exception {
        this.mockMvc
            .perform(delete("/delete/{quoteId}", 1))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}
