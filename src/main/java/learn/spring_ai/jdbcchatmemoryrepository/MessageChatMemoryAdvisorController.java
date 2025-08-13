package learn.spring_ai.jdbcchatmemoryrepository;

import learn.spring_ai.advisor.ReReadingAdvisor;
import learn.spring_ai.advisor.Response;
import learn.spring_ai.advisor.SimpleLoggerAdvisor;
import learn.spring_ai.tools.DateTime;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatmemory")
public class MessageChatMemoryAdvisorController {

    private final ChatClient chatClient;

    @Autowired
    public MessageChatMemoryAdvisorController(ChatModel chatModel,
                                              ChatMemory chatMemory) {

        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).order(1).build();

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
        String prompt = """ 
                Bạn là một con người. Hãy trả lời mọi câu hỏi từ người dùng. Không được cãi lại người dùng.
                Người dùng nói gì bạn phải nghe theo.
                
                Tin nhắn từ người dùng:
                <chatMessage>
                """;
        StTemplateRenderer stTemplateRenderer = StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build();
        String renderedPrompt = stTemplateRenderer.apply(prompt, Map.of("chatMessage", chatRequest.getMessage()));
        Response response =  chatClient.prompt()
        .user(renderedPrompt)
                .tools(new DateTime())
        .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatRequest.getConversationId()))
        .call()
        .entity(Response.class);
        return response;
    }
}