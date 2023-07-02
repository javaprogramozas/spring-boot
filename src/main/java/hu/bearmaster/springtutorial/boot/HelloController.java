package hu.bearmaster.springtutorial.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${hello}")
    private List<String> helloArgs;

    @Value("${" + CommandLinePropertySource.DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME + "}")
    private List<String> nonOptionArgs;

    @GetMapping("/hello")
    public String hello(Model model) {
        LOGGER.info("Hello {}", helloArgs);
        LOGGER.info("Hello2 {}", nonOptionArgs);
        Integer userCount = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        model.addAttribute("count", userCount);
        return "hello";
    }
}
