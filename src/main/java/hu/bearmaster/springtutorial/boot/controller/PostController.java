package hu.bearmaster.springtutorial.boot.controller;

import hu.bearmaster.springtutorial.boot.model.Post;
import hu.bearmaster.springtutorial.boot.model.exception.NotFoundException;
import hu.bearmaster.springtutorial.boot.model.properties.PostProperties;
import hu.bearmaster.springtutorial.boot.model.request.CreatePostRequest;
import hu.bearmaster.springtutorial.boot.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;
    private final PostProperties postProperties;
    private final MessageSource messageSource;

    public PostController(PostService postService, PostProperties postProperties, MessageSource messageSource) {
        this.postService = postService;
        this.postProperties = postProperties;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/post/{id}")
    public Post getPostById(@PathVariable long id,
                            @RequestParam(required = false, defaultValue = "en", name = "locale") String requestLocale) {
        Locale locale = new Locale(requestLocale);
        LOGGER.info("Selected locale: {}", locale);
        String message = messageSource.getMessage("post.not_found", new Object[]{id}, locale);
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new NotFoundException(message));
        return post;
    }

    @GetMapping(value = "/post/settings")
    public PostProperties getPostSettings() {
        return postProperties;
    }

    @PostMapping(value = "/post")
    public Post createNewPost(@RequestBody @Valid CreatePostRequest request) {
        return postService.createNewPost(request);
    }

    @PostMapping("/post/report")
    public ResponseEntity<Map<String, String>> generateReport() {
        postService.generateReport();
        return ResponseEntity.accepted().body(Map.of("message", "Report generation started"));
    }

    @PostMapping("/post/schedule")
    public ResponseEntity<Map<String, String>> schedule(@RequestParam String cron) {
        return ResponseEntity.accepted().body(
                Map.of("message", "Report scheduled"));
    }
}
