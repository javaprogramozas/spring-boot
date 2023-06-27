package hu.bearmaster.springtutorial.boot;

import hu.bearmaster.springtutorial.boot.model.UserContext;
import hu.bearmaster.springtutorial.boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@SpringBootApplication
public class SpringBootTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTutorialApplication.class, args);
	}

	@Bean
	@SessionScope
	// for demo purposes only!
	public UserContext userContext(UserService userService, HttpServletRequest request) {
		long userId = Optional.ofNullable(request.getParameter("userId"))
				.map(Long::valueOf)
				.orElse(-1L);
		UserContext userContext = new UserContext();
		userContext.setCurrentUser(userService.getUserById(userId).orElse(null));
		return userContext;
	}

}
