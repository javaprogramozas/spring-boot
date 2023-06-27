package hu.bearmaster.springtutorial.boot.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UserContext {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final ZonedDateTime createdAt;

    private User currentUser;

    public UserContext() {
        this.createdAt = ZonedDateTime.now();
    }

    @Override
    public String toString() {
        return "created @ " + createdAt.format(TIME_FORMAT);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
