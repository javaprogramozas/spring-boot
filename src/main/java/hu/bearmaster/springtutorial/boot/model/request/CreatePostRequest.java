package hu.bearmaster.springtutorial.boot.model.request;

import hu.bearmaster.springtutorial.boot.validation.LowerCase;
import jakarta.validation.constraints.NotBlank;

public class CreatePostRequest {

    @NotBlank(message = "{cannot_be_blank}")
    private String title;

    private String description;

    private String slug;

    private long authorId;

    @LowerCase
    private String topic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
