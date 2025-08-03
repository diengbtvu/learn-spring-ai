package learn.spring_ai.advisor;

public class UserRequest {
    private String message;
    private String name;
    private String role;

    public UserRequest() {
    }
    public UserRequest(String message, String name, String role) {
        this.message = message;
        this.name = name;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
