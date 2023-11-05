package hu.bearmaster.springtutorial.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bearmaster.springtutorial.boot.model.Post;
import hu.bearmaster.springtutorial.boot.model.request.CreatePostRequest;
import hu.bearmaster.springtutorial.boot.service.PostService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Captor
    private ArgumentCaptor<CreatePostRequest> requestCaptor;

    @Test
    void getPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test post title");
        post.setDescription("Test post description");
        Mockito.when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/post/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\": 1}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Test post title")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Post actualPost = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Post.class);
        Assertions.assertThat(actualPost.getId()).isEqualTo(1);
    }

    @Test
    void createNewPost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test post title");
        post.setDescription("Test post description");
        Mockito.when(postService.createNewPost(requestCaptor.capture())).thenReturn(post);

        String requestBody = "{\"title\": \"Test title\"}";

        mvc.perform(MockMvcRequestBuilders.post("/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(Matchers.is("Test post title")));

        CreatePostRequest postRequest = requestCaptor.getValue();
        Assertions.assertThat(postRequest.getTitle()).isEqualTo("Test title");
    }

}
