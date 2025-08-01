package learn.spring_ai.prompttemplate;

import learn.spring_ai.chatResponse.ChatResponseTest;
import learn.spring_ai.returnentity.ActorFilmsEntity;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/promptemplate")
public class PromptTemplates {
    private ChatClient chatClient;
    private static final Logger log = LoggerFactory.getLogger(PromptTemplates.class);
    private StTemplateRenderer stTemplateRenderer = StTemplateRenderer.builder().endDelimiterToken('>').startDelimiterToken('<').build();

    public PromptTemplates(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/renderchatclient")
    public ActorFilmsEntity promptTemplate(String actor) {
        ActorFilmsEntity actorFilmsEntity = chatClient.prompt()
                .user(
                        u -> u
                                .text("Hãy cho tôi tên của 5 tác phẩm của đạo diễn <actorName>")
                                .param("actorName",actor)

                )
                .templateRenderer(
                        StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build()
                )
                .call()
                .entity(ActorFilmsEntity.class);
            return  actorFilmsEntity;
    }
    @GetMapping("/renderthucong")
    public ActorFilmsEntity promptTemplateRednder(String actor) {
        String template = "Hãy cho tôi tên của 5 tác phẩm của đạo diễn <actorName>";
        String rendered = stTemplateRenderer.apply(template, Map.of("actorName",actor));
        log.info("Rendered template:\n{}\n rendered:\n{}",template,rendered);
        ActorFilmsEntity actorFilmsEntity = chatClient.prompt()
                .user(rendered)
                .call()
                .entity(ActorFilmsEntity.class);
        return  actorFilmsEntity;
    }


}
