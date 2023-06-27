package hu.bearmaster.springtutorial.boot.model.request;

import hu.bearmaster.springtutorial.boot.model.UserStatus;

public class CreateUserRequest {

    private String username;
    private UserStatus status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
