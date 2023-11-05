package hu.bearmaster.springtutorial.boot.controller;

import hu.bearmaster.springtutorial.boot.model.User;
import hu.bearmaster.springtutorial.boot.service.UselessFactService;
import hu.bearmaster.springtutorial.boot.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UselessFactService factService;

    @Test
    void getUserById() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("test-user");

        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mvc.perform(MockMvcRequestBuilders.get("/user/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", user))
                .andExpect(MockMvcResultMatchers.request().sessionAttribute("visitedUserId", Matchers.is(1L)));
    }
}
