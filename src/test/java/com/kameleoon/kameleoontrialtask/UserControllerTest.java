package com.kameleoon.kameleoontrialtask;

import com.kameleoon.kameleoontrialtask.dto.UserDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    public void addNewUserShouldReturnStatus200AndExactView() throws Exception {
        this.mockMvc
            .perform(get("/register"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("register"));
    }

    @Test
    public void saveNewUserShouldReturnStatus3xxAndRedirect() throws Exception {
        Mockito.when(userService.saveNewUser(new UserDTO())).thenReturn(true);

        this.mockMvc
            .perform(post("/save_new_user"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    public void getUserInfoShouldReturnStatus200AndExactView() throws Exception {
        Mockito.when(userService.getUsernameFromSecurityContextHolder()).thenReturn("admin");
        Mockito.when(userService.findByUsername("admin")).thenReturn(new UserDTO());

        this.mockMvc
            .perform(get("/profile/{name}", "admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("profile"));
    }
}
