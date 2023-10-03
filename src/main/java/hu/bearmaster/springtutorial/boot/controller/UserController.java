package hu.bearmaster.springtutorial.boot.controller;

import hu.bearmaster.springtutorial.boot.model.User;
import hu.bearmaster.springtutorial.boot.model.UserContext;
import hu.bearmaster.springtutorial.boot.model.exception.NotFoundException;
import hu.bearmaster.springtutorial.boot.model.request.CreateUserRequest;
import hu.bearmaster.springtutorial.boot.model.response.FactResponse;
import hu.bearmaster.springtutorial.boot.service.UselessFactService;
import hu.bearmaster.springtutorial.boot.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Locale;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UselessFactService factService;
    private final UserContext userContext;

    public UserController(UserService userService, UselessFactService factService, UserContext userContext) {
        this.userService = userService;
        this.factService = factService;
        this.userContext = userContext;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, @SessionAttribute(required = false) Long visitedUserId,
                              @SessionAttribute(required = false) User latestUser, Locale locale) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("highlighted", visitedUserId);
        LOGGER.info("User context: {}", userContext);
        LOGGER.info("Current user: {}", userContext.getCurrentUser());

        FactResponse fact = factService.getFact("today", locale);
        model.addAttribute("fact", fact);

        return "users";
    }

    @GetMapping("/user/{id}")
    public String getUserById(Model model, @PathVariable long id, HttpSession session) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        model.addAttribute("user", user);

        if (user != null) {
            session.setAttribute("visitedUserId", user.getId());
        }

        return "user";
    }

    @GetMapping("/addUser")
    public String addUserPage(Model model) {
        model.addAttribute("user", new CreateUserRequest());
        return "add_user";
    }

    @PostMapping("/user")
    public String addUser(CreateUserRequest request, HttpSession session) {
        User createdUser = userService.createUser(request);
        session.setAttribute("latestUser", createdUser);
        return "redirect:/user/" + createdUser.getId();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String handleNotFound(NotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/4xx";
    }
}
