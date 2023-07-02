package hu.bearmaster.springtutorial.boot;

import hu.bearmaster.springtutorial.boot.model.UserContext;
import hu.bearmaster.springtutorial.boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@SpringBootApplication
public class SpringBootTutorialApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootTutorialApplication.class);

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootTutorialApplication.class, args);
		new SpringApplicationBuilder()
				.sources(SpringBootTutorialApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.lazyInitialization(true)
				.listeners(event -> logEvent(event))
				.run(args);
	}

	private static void logEvent(ApplicationEvent event) {
		LOGGER.info("*** event: {}", event.getClass().getSimpleName());
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
