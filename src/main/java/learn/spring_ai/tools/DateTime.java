package learn.spring_ai.tools;

import org.springframework.ai.tool.annotation.Tool;

public class DateTime {
    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return java.time.LocalDateTime.now().toString();
    }
}
