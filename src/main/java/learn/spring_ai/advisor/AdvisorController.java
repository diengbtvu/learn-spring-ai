package learn.spring_ai.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/advisor")
public class AdvisorController {
    private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

    private final ChatClient chatClient;

    public AdvisorController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        new ReReadingAdvisor().withOrder(1)
                ).build();
    }

    @PostMapping("/askRE2")
    public Response ask(UserRequest userRequest) {
        String message = userRequest.getMessage();
        String name = userRequest.getName();
        String role = userRequest.getRole();
        logger.info("Received request: message={}, name={}, role={}", message, name, role);
        Response res = chatClient.prompt().user(userRequest.getMessage()).call().entity(Response.class);
        logger.info("Response: {}", res);
        return res;
    }

}
