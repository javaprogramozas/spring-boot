package hu.bearmaster.springtutorial.boot.service;

import hu.bearmaster.springtutorial.boot.TestConfig;
import hu.bearmaster.springtutorial.boot.model.Post;
import hu.bearmaster.springtutorial.boot.model.User;
import hu.bearmaster.springtutorial.boot.model.UserStatus;
import hu.bearmaster.springtutorial.boot.model.request.CreatePostRequest;
import hu.bearmaster.springtutorial.boot.model.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PostService.class, UserService.class, TestConfig.class})
@TestPropertySource(locations = "/test.properties")
@Sql(scripts = "/test-data.sql")
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void getPostByIdShouldReturnPost() {

        Optional<Post> actual = postService.getPostById(1);

        assertThat(actual).hasValueSatisfying(actualPost -> {
            assertThat(actualPost.getId()).isEqualTo(1);
            assertThat(actualPost.getTitle()).isEqualTo("Test title");
            assertThat(actualPost.getDescription()).isEqualTo("Test description");
        });
    }

    @Test
    void createNewPostShouldPersistAPost() {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("Post title");
        request.setDescription("Post description");
        request.setAuthorId(1L);
        request.setSlug("test-slug");
        request.setTopic("test topic");

        Post actual = postService.createNewPost(request);

        assertThat(actual.getTitle()).isEqualTo("Post title");
        assertThat(actual.getDescription()).isEqualTo("Post description");
        assertThat(actual.getAuthor().getId()).isEqualTo(1);
        assertThat(actual.getAuthor().getUsername()).isEqualTo("test-user");
        assertThat(actual.getSlug()).isEqualTo("test-slug");
        assertThat(actual.getTopic()).isEqualTo("test topic");
    }

    @Test
    void createUserShouldPersistANewUserWithDefaultStatus() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");

        User actual = userService.createUser(request);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUsername()).isEqualTo("testuser");
        assertThat(actual.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getPosts()).isEmpty();
    }

}