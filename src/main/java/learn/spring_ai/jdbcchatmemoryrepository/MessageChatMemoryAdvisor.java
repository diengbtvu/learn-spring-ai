package learn.spring_ai.jdbcchatmemoryrepository;

import learn.spring_ai.advisor.ReReadingAdvisor;
import learn.spring_ai.advisor.Response;
import learn.spring_ai.advisor.SimpleLoggerAdvisor;
import learn.spring_ai.tools.DateTime;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatmemory")
public class MessageChatMemoryAdvisor {

    private final ChatClient chatClient;

    @Autowired
    public MessageChatMemoryAdvisor(ChatModel chatModel,
                                    ChatMemory chatMemory) {

        org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor memoryAdvisor = org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor.builder(chatMemory).order(1).build();

        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(List.of(
                        new SimpleLoggerAdvisor(),    // advisor log
                        memoryAdvisor,                // advisor chat memory
                        new ReReadingAdvisor().withOrder(1) // advisor RE2
                ))

                .build();
    }

    @RequestMapping("/ask")
    @GetMapping
    public Response ask(ChatRequest chatRequest){
        Response response =  chatClient.prompt()
        .user(chatRequest.getMessage())
                .tools(new DateTime())
        .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatRequest.getConversationId()))
        .call()
        .entity(Response.class);
        return response;
    }
}