package learn.spring_ai.jdbcchatmemoryrepository;

import learn.spring_ai.advisor.ReReadingAdvisor;
import learn.spring_ai.advisor.Response;
import learn.spring_ai.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatmemory")
public class ChatMemoryController {

    private final ChatClient chatClient;

    @Autowired
    public ChatMemoryController(ChatModel chatModel,
                                ChatMemory chatMemory) {

        // Tạo MessageChatMemoryAdvisor qua builder (constructor private)
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(
                        new SimpleLoggerAdvisor(),    // advisor ghi log
                        memoryAdvisor,                // advisor lưu chat memory
                        new ReReadingAdvisor().withOrder(1) // advisor đọc lại lịch sử
                ))
                .build();
    }

    @RequestMapping("/ask")
    @GetMapping
    public Response ask(ChatRequest chatRequest){
        Response response =  chatClient.prompt()
        .user(chatRequest.getMessage())
        .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatRequest.getConversationId()))
        .call()
        .entity(Response.class);
        return response;
    }
}