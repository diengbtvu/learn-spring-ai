package learn.spring_ai.chatResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("/chatResponeTest")
public class ChatResponseTest {

    private static final Logger log = LoggerFactory.getLogger(ChatResponseTest.class);
    private ChatClient chatClient;

    public ChatResponseTest(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("chatResponse")
    public String chatResponeTest(String message){
        log.atInfo().log("Message user: " + message);
        ChatResponse response = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
        return response.getResult().getOutput().getText();
    }
}
