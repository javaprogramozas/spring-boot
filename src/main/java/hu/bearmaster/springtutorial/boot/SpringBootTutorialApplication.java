package hu.bearmaster.springtutorial.boot;

import hu.bearmaster.springtutorial.boot.model.UserContext;
import hu.bearmaster.springtutorial.boot.model.properties.PostProperties;
import hu.bearmaster.springtutorial.boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties(PostProperties.class)
@EnableAsync
@EnableScheduling
public class SpringBootTutorialApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootTutorialApplication.class);

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootTutorialApplication.class, args);
		new SpringApplicationBuilder()
				.sources(SpringBootTutorialApplication.class)
				.bannerMode(Banner.Mode.OFF)
				//.lazyInitialization(true)
				//.listeners(event -> logEvent(event))
				//.profiles("local")
				.run(args);
	}

	private static void logEvent(ApplicationEvent event) {
		LOGGER.info("*** event: {}", event.getClass().getSimpleName());
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return arguments -> {
			LOGGER.info("Raw arguments: {}", (Object) arguments.getSourceArgs());
			LOGGER.info("Option names: {}", arguments.getOptionNames());
			if (arguments.containsOption("hello")) {
				List<String> helloArguments = arguments.getOptionValues("hello");
				LOGGER.info("Hello {}", helloArguments);
			}
			LOGGER.info("Non-options: {}", arguments.getNonOptionArgs());
		};
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
