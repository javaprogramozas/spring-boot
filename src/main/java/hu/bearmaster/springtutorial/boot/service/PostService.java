package hu.bearmaster.springtutorial.boot.service;

import hu.bearmaster.springtutorial.boot.model.Post;
import hu.bearmaster.springtutorial.boot.model.request.CreatePostRequest;
import hu.bearmaster.springtutorial.boot.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(long id) {
        return postRepository.findById(id);
    }

    public Post createNewPost(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setCreatedOn(ZonedDateTime.now());
        post.setAuthor(userService.getUserById(request.getAuthorId()).orElseThrow());
        post.setSlug(request.getSlug());
        post.setTopic(request.getTopic());
        return postRepository.save(post);
    }

    @Async
    public void generateReport() {
        //taskExecutor.execute(this::reportGeneratorTask);
        reportGeneratorTask();
    }

    private void reportGeneratorTask() {
        LOGGER.info("Generating post report");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOGGER.error("Post report generation was interrupted!");
            return;
        }
        long numberOfPosts = postRepository.count();
        LOGGER.info("Report generated of {} posts", numberOfPosts);
    }
}
