package hu.bearmaster.springtutorial.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/hello")
    public String hello(Model model) {
        Integer userCount = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        model.addAttribute("count", userCount);
        return "hello";
    }
}
