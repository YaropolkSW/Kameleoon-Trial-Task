package com.kameleoon.kameleoontrialtask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kameleoon.kameleoontrialtask.dto.UserDTO;
import com.kameleoon.kameleoontrialtask.rest.UserRestController;
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
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class UserRestControllerTest {
    private final static String EMPTY_LINE = "";
    private final static String PATH_TO_UNCORRECT_USER_INFO_JSON = "src/test/resources/uncorrect-user-info.json";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRestController userRestController;
    @MockBean
    private UserService userService;

    @Test
    public void registerNewUserShouldReturnStatus200AndCorrectJSON() throws Exception {
        final UserDTO expectedUser = UserDTO.builder()
            .username("user-test")
            .email("user-test@gmail.com")
            .password("user-test")
            .dateOfCreation(LocalDate.now())
            .build();

        final UserDTO actualUser = (UserDTO) userRestController.registerNewUser(expectedUser);

        final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        final String json = mapper.writeValueAsString(expectedUser);

        this.mockMvc
            .perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(json))
            .andDo(print())
            .andExpect(status().isOk());

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    public void getUserInfoShouldReturnStatus200AndCorrectJSON() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");

        final String json = String.join(EMPTY_LINE, Files.readAllLines(Paths.get(PATH_TO_UNCORRECT_USER_INFO_JSON)));

        this.mockMvc
            .perform(get("/api/users/info/{userId}", 1))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(json));
    }
}
