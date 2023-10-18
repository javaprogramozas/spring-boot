package hu.bearmaster.springtutorial.boot.service;

import hu.bearmaster.springtutorial.boot.model.User;
import hu.bearmaster.springtutorial.boot.model.UserStatus;
import hu.bearmaster.springtutorial.boot.model.request.CreateUserRequest;
import hu.bearmaster.springtutorial.boot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.env.MockEnvironment;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private MockEnvironment mockEnvironment = new MockEnvironment();

    private UserService userService;

    @BeforeEach
    void setUp() {
        mockEnvironment.setProperty("user.default-status", "PENDING");
        this.userService = new UserService(userRepository, mockEnvironment);
    }

    @Test
    void getUserByIdShouldReturnUser() {
        User testUser = new User("testuser", UserStatus.ACTIVE, ZonedDateTime.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> actual = userService.getUserById(1);

        assertThat(actual).hasValue(testUser);
    }

    @Test
    void createUserShouldPersistANewUserWithGivenStatus() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setStatus(UserStatus.ACTIVE);

        when(userRepository.save(any())).thenAnswer(returnsFirstArg());

        User actual = userService.createUser(request);

        assertThat(actual.getId()).isNull();
        assertThat(actual.getUsername()).isEqualTo("testuser");
        assertThat(actual.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getPosts()).isEmpty();
    }

    @Test
    void createUserShouldPersistANewUserWithDefaultStatus() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");

        when(userRepository.save(any())).thenAnswer(returnsFirstArg());

        User actual = userService.createUser(request);

        assertThat(actual.getId()).isNull();
        assertThat(actual.getUsername()).isEqualTo("testuser");
        assertThat(actual.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getPosts()).isEmpty();
    }

}