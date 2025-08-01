package learn.spring_ai.returnentity;

import learn.spring_ai.chatResponse.ChatResponseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/returnentity")
public class ReturnEntityTest {
    private static final Logger log = LoggerFactory.getLogger(ChatResponseTest.class);
    private ChatClient chatClient;

    public ReturnEntityTest(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/actor")
    public ActorFilmsEntity returnEntityTest(String message){
        ActorFilmsEntity actorFilmsEntity = new ActorFilmsEntity();
        actorFilmsEntity = chatClient.prompt()
                .user(message)
                .call()
                .entity(ActorFilmsEntity.class);
        return actorFilmsEntity;
    }
    @GetMapping("/actors")
    public List<ActorFilmsEntity> returnEntitiesTest(String message){
        List<ActorFilmsEntity> actorFilms = chatClient.prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<ActorFilmsEntity>>() {});
        return actorFilms;
    }
}
