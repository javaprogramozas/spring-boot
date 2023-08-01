package hu.bearmaster.springtutorial.boot.controller;

import hu.bearmaster.springtutorial.boot.model.Post;
import hu.bearmaster.springtutorial.boot.model.exception.NotFoundException;
import hu.bearmaster.springtutorial.boot.model.properties.PostProperties;
import hu.bearmaster.springtutorial.boot.model.request.CreatePostRequest;
import hu.bearmaster.springtutorial.boot.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PostController {

    private final PostService postService;
    private final PostProperties postProperties;

    public PostController(PostService postService, PostProperties postProperties) {
        this.postService = postService;
        this.postProperties = postProperties;
    }

    @GetMapping(value = "/post/{id}")
    public Post getPostById(@PathVariable long id) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
        return post;
    }

    @GetMapping(value = "/post/settings")
    public PostProperties getPostSettings() {
        return postProperties;
    }

    @PostMapping(value = "/post")
    public Post createNewPost(@RequestBody CreatePostRequest request) {
        return postService.createNewPost(request);
    }

    @PostMapping("/post/report")
    public ResponseEntity<Map<String, String>> generateReport() {
        postService.generateReport();
        return ResponseEntity.accepted().body(Map.of("message", "Report generation started"));
    }
}
